import * as tf from '@tensorflow/tfjs-node';
import path from 'path';
import { fileURLToPath } from 'url';
import { constants as httpStatus } from 'http2';
import UsersRepository from '../repository/Users.js';
import { isValidUUID, roundToTwoDecimals } from '../pkg/utils.js';
import { CreateDepression } from '../repository/Depression.js';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const modelPath = path.resolve(__dirname, '../ModelML/Diagnosis/model.json');

export const ModelDiagnosis = async (req, res) => {
    try {
        const body = req.body;
        // Validate required fields
        const requiredFields = [
            'age', 'gender', 'work_pressure', 'job_satisfaction', 
            'sleep_duration', 'dietary_habits', 
            'have_you_ever_had_suicidal_thoughts', 
            'work_hours', 'financial_stress', 
            'family_history_of_mental_illness'
        ];

        // Check for missing or undefined fields
        const missingFields = requiredFields.filter(field => {
            const value = body.data[field];
            return value === undefined || value === null || value === '';
        });

        if (missingFields.length > 0) {
            return res.status(400).json({
                status: 400,
                message: `Missing required fields: ${missingFields.join(', ')}`,
                missingFields: missingFields,
            });
        }

        // Validate that all fields are numbers
        const invalidFields = requiredFields.filter(field => {
            const value = body.data[field];
            return isNaN(Number(value));
        });

        if (invalidFields.length > 0) {
            return res.status(400).json({
                status: 400,
                message: `Invalid values for fields: ${invalidFields.join(', ')}`,
                invalidFields: invalidFields,
            });
        }

        // add uuid check is uuid valid
        if (!isValidUUID(body.user_id)) {
            console.log('Invalid user ID');
            return res.status(httpStatus.HTTP_STATUS_BAD_REQUEST).json({
                status: httpStatus.HTTP_STATUS_BAD_REQUEST,
                message: 'Invalid user ID',
            });
        }
        

        // Check if the user exists
        const user = await UsersRepository.GetUserByID(body.user_id);
        if (user.err) {
            return res.status(user.status).json({
                err: user.err,
                message: user.message,
                status: user.status,
            });
        }

        // // Add MinMaxScaler function to scale the Age feature
        // function manualMinMaxScaler(data, featureRange = [0, 1]) {
        //     // Calculate the minimum and maximum values of each column
        //     const dataMin = data[0].map((_, colIndex) => Math.min(...data.map(row => row[colIndex])));
        //     const dataMax = data[0].map((_, colIndex) => Math.max(...data.map(row => row[colIndex])));
        //     const [minRange, maxRange] = featureRange;

        //     // Avoid division by zero for constant features
        //     const rangeDiff = dataMax.map((max, index) => (max - dataMin[index]) || 1);

        //     // Scale the data
        //     const scaledData = data.map(row => 
        //         row.map((value, colIndex) => 
        //             ((value - dataMin[colIndex]) / rangeDiff[colIndex]) * (maxRange - minRange) + minRange
        //         )
        //     );
            
        //     return scaledData;
        // }

        function manualMinMaxScaler(data, featureRange = [0, 1]) {
            const dataMin = data[0].map((_, colIndex) => Math.min(...data.map(row => row[colIndex])));
            const dataMax = data[0].map((_, colIndex) => Math.max(...data.map(row => row[colIndex])));
            const [minRange, maxRange] = featureRange;
        
            // Special case for age scaling: map 10 to 0 and 45 to 1
            const ageMin = 10;
            const ageMax = 45;
        
            // Scale the age feature specifically (for Age column)
            const scaledAge = data.map(row => {
                const age = row[0];  // Assuming Age is in the first column of the data array
                const scaled = (age - ageMin) / (ageMax - ageMin);
                row[0] = scaled; // Update the age with the scaled value
                return row;
            });
        
            // Standard scaling for other columns
            const rangeDiff = dataMax.map((max, index) => (max - dataMin[index]) || 1);
            
            // Scale all columns
            const scaledData = scaledAge.map(row => 
                row.map((value, colIndex) => 
                    colIndex === 0 ? value : ((value - dataMin[colIndex]) / rangeDiff[colIndex]) * (maxRange - minRange) + minRange
                )
            );
        
            return scaledData;
        }
        


        // Change the data value to an array

        var newData = {
            "Age": [body.data.age],
            "Gender": [body.data.gender],
            "Work Pressure": [body.data.work_pressure],
            "Job Satisfaction": [body.data.job_satisfaction],
            "Sleep Duration": [body.data.sleep_duration],
            "Dietary Habits": [body.data.dietary_habits],
            "Have you ever had suicidal thoughts ?": [body.data.have_you_ever_had_suicidal_thoughts],
            "Work Hours": [body.data.work_hours],
            "Financial Stress": [body.data.financial_stress],
            "Family History of Mental Illness": [body.data.family_history_of_mental_illness]
        };

        // Ensure TensorFlow.js is imported
        if (!tf) {
            throw new Error('TensorFlow.js is not properly imported');
        }

        // Load the model
        const handler = tf.io.fileSystem(modelPath);
        const model = await tf.loadLayersModel(handler);

        // Scale Age feature using MinMaxScaler (optional, depending on model needs)
        const ageMinMaxScaled = manualMinMaxScaler([newData.Age], [0, 1]);
        newData.Age = ageMinMaxScaled[0]; // Apply the scaled age back to the data
        console.log("Scaled Age Values:", newData.Age);
        body.data.age = newData.Age[0];

        // Prepare input tensor
        const columns = Object.keys(newData);
        const patientArray = columns.map(column => newData[column][0]);
        const patientTensor = tf.tensor2d([patientArray], [1, columns.length]);

        // Make prediction
        const prediction = model.predict(patientTensor);
        const predictionData = await prediction.data();

        // Cleanup tensors
        patientTensor.dispose();
        prediction.dispose();

        // Convert prediction data to numbers and round them
        const formattedPrediction = Array.from(predictionData).map(value => roundToTwoDecimals(Number(value)));

        // Determine diagnosis based on result
        const diagnosis = userThreshold(body.data,formattedPrediction[0]);

        body.data.output = formattedPrediction[0];
        const insertDepression = await CreateDepression(body.user_id,body.data);
        if (insertDepression.status !== 201) {
            return res.status(insertDepression.status).json(insertDepression);
        }
        
        // Return prediction result
        return res.status(diagnosis.status).json(diagnosis);
    } catch (error) {
        console.error('Error in ModelDiagnosis:', error);
        console.error('Error stack:', error.stack);
        throw error;
    }
};

function userThreshold(bodyData,result) {
    const risk_factors = [];

    if (bodyData.age >= 0.6) {
        risk_factors.push("Older age may increase the risk of depression");
    } else if (bodyData.age <= 0.4) {
        risk_factors.push("Young age may be vulnerable to mental stress");
    }

    // Work Pressure Risk Factors
    if (bodyData.work_pressure >= 4) {
        risk_factors.push("Very high work pressure");
    } else if (bodyData.work_pressure >= 3) {
        risk_factors.push("High work pressure (Needs attention)");
    }

    // Job Satisfaction Risk Factors
    if (bodyData.job_satisfaction <= 2) {
        risk_factors.push("Very low job satisfaction level");
    } else if (bodyData.job_satisfaction <= 3) {
        risk_factors.push("Low job satisfaction level");
    }

    // Sleep Duration Risk Factors
    if (bodyData.sleep_duration === 0) {
        risk_factors.push("Very insufficient sleep duration (<5 hours)");
    } else if (bodyData.sleep_duration === 1) {
        risk_factors.push("Insufficient sleep duration (5-6 hours)");
    }

    // Dietary Habits Risk Factors
    if (bodyData.dietary_habits === 0) {
        risk_factors.push("Unhealthy dietary habits");
    } else if (bodyData.dietary_habits === 1) {
        risk_factors.push("Dietary habits need improvement");
    }

    // Suicidal Thoughts Risk Factors
    if (bodyData.have_you_ever_had_suicidal_thoughts === 1) {
        risk_factors.push("Detected suicidal thoughts (Requires immediate professional intervention)");
    }

    // Work Hours Risk Factors
    if (bodyData.work_hours >= 12) {
        risk_factors.push("Excessive working hours (>12 hours)");
    } else if (bodyData.work_hours >= 10) {
        risk_factors.push("High working hours (>10 hours)");
    }

    // Financial Stress Risk Factors
    if (bodyData.financial_stress >= 4) {
        risk_factors.push("Very high financial stress");
    } else if (bodyData.financial_stress >= 3) {
        risk_factors.push("High financial stress");
    }

    // Family History Risk Factors
    if (bodyData.family_history_of_mental_illness === 1) {
        risk_factors.push("Family history of mental illness detected");
    }

    // Determine diagnosis based on result
    let diagnosis = "";
    if (result <= 0.3) {
        diagnosis = "Low Risk";
    } else if (result <= 0.6) {
        diagnosis = "Moderate Risk";
    } else {
        diagnosis = "High Risk";
    }
    console.log(result);  // Untuk memeriksa nilai result

    // Construct response JSON
    const responseJSON = {
        status: 200,
        message: 'Prediction successful',
        diagnosis: diagnosis,
        confidence: `${(result * 100).toFixed(2)}%`,
        risks_factors: risk_factors.length > 0 
            ? risk_factors 
            : ["No significant risk factors detected"]
    };

    return responseJSON;
}

