import Depression from "../models/DepressionModel.js";
import { v4 as uuidv4 } from 'uuid';

export const GetDepressionByUserID= async (userId) => {
    try {
        const user = await Depression.findOne({
            where: {
                user_id: userId
            }
        });

        if (!user) {
            return {
                status: 404,
                message: 'User not found',
                err: null,
            };
        }

        return {
            status: 200,
            message: 'User found',
            err: null,
            data: user,
        };
    } catch (error) {
        console.error('Error in GetUserByID:', error);
        console.error('Error stack:', error.stack);
        return {
            status: 500,
            message: 'Internal server error',
            err: error,
        };
    }
};

export const GetDepressionByID = async (id) => {
    try {
        const user = await Depression.findOne({
            where: {
                id: id
            }
        });

        if (!user) {
            return {
                status: 404,
                message: 'User not found',
                err: null,
            };
        }

        return {
            status: 200,
            message: 'User found',
            err: null,
            data: user,
        };


    } catch (error) {
        console.error('Error in GetUserByID:', error);
        console.error('Error stack:', error.stack);
        return {
            status: 500,
            message: 'Internal server error',
            err: error,
        };
    }
};

export const CreateDepression = async (userId, data) => {
    try {
        const insertDepression = await Depression.create({
            id: uuidv4(),
            user_id: userId,
            age: data.age,
            gender: data.gender,
            work_pressure: data.work_pressure,
            job_satisfaction: data.job_satisfaction,
            sleep_duration: data.sleep_duration,
            dietary_habits: data.dietary_habits,
            have_you_ever_had_suicidal_thoughts: data.have_you_ever_had_suicidal_thoughts,
            work_hours: data.work_hours,
            financial_stress: data.financial_stress,
            family_history_of_mental_illness: data.family_history_of_mental_illness,
            output: data.output
        });

        if (!insertDepression) {
            return {
                status: 400,
                message: 'Failed to insert data',
                err: null,
            };
        }

        return {
            status: 201,
            message: 'data created successfully',
            data: insertDepression,
        };
    } catch (error) {
        console.error('Error in CreateUser:', error);
        console.error('Error stack:', error.stack);
        return {
            status: 500,
            message: 'Internal server error',
            err: error,
        };
    }
};