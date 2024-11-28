import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import tensorflow as tf
from tensorflow import keras
from tensorflow.keras.callbacks import EarlyStopping
from tensorflow.keras import layers, regularizers
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler, LabelEncoder, MinMaxScaler
from imblearn.over_sampling import SMOTE

df = pd.read_csv("../dataset/mental_health_diagnosis_treatment_.csv")
df_2 = pd.read_csv("../dataset/synthetic_mental_health_data.csv")

df = pd.concat([df, df_2], ignore_index=True)

df.head()

df= df.drop(['Patient ID', 'Medication', 'Therapy Type','Treatment Start Date','Treatment Duration (weeks)','Outcome','Treatment Progress (1-10)','AI-Detected Emotional State','Adherence to Treatment (%)'], axis=1)

df.sample()

df.isna().sum()

encoder = LabelEncoder()
df['Gender'] = encoder.fit_transform(df['Gender'])

print("Mapping Kategori ke Numerik:")
for i, label in enumerate(encoder.classes_):
    print(f"{label} -> {i}")

# severity_order = ['Generalized Anxiety', 'Panic Disorder',
#                   'Major Depressive Disorder', 'Bipolar Disorder']
# df['Diagnosis'] = pd.Categorical(df['Diagnosis'], categories=severity_order, ordered=True)
# for diagnosis in severity_order:
#     df[diagnosis] = (df['Diagnosis'] == diagnosis).astype(int)
# # Menampilkan hasil tanpa kolom Severity_Label
# df.drop(columns=['Diagnosis'], inplace=True)

severity_mapping = {
    'Generalized Anxiety': 0,
    'Panic Disorder': 1,
    'Major Depressive Disorder': 2,
    'Bipolar Disorder': 3
}

df['Diagnosis'] = df['Diagnosis'].map(severity_mapping)

# Mengurutkan DataFrame berdasarkan severity_order
df['Diagnosis'] = pd.Categorical(df['Diagnosis'])
df['Diagnosis'] = encoder.fit_transform(df['Diagnosis'].astype(str))

print("Mapping Kategori ke Numerik:")
for i, label in enumerate(encoder.classes_):
    print(f"{label} -> {i}")

df.head()

columns_to_normalize = ['Age','Gender','Symptom Severity (1-10)', 'Mood Score (1-10)', 'Sleep Quality (1-10)',
                        'Stress Level (1-10)','Physical Activity (hrs/week)']
scaler = MinMaxScaler()
df[columns_to_normalize] = scaler.fit_transform(df[columns_to_normalize])
print(df[columns_to_normalize].head())

df.head()

correlation_matrix = df.corr()

plt.figure(figsize=(8, 6))
sns.heatmap(correlation_matrix, annot=True, cmap='coolwarm')
plt.title('Heatmap Korelasi')
plt.show()

# Memisahkan fitur dan label
X = df[['Age','Gender','Symptom Severity (1-10)','Mood Score (1-10)', 'Sleep Quality (1-10)',
        'Physical Activity (hrs/week)','Stress Level (1-10)']]
y = df['Diagnosis']

X.sample()

smote = SMOTE(random_state=42)
X, y = smote.fit_resample(X, y)

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.1, shuffle=True)
print(f"Train data: {X_train.shape}, {y_train.shape}")
print(f"Test data: {X_test.shape}, {y_test.shape}")

scaler = StandardScaler()
X_train = scaler.fit_transform(X_train)
X_test = scaler.transform(X_test)

y_train = keras.utils.to_categorical(y_train, num_classes=4)
y_test = keras.utils.to_categorical(y_test, num_classes=4)
print(y_train[:1])

y.sample()

y_train.shape

model = tf.keras.Sequential([
    tf.keras.Input(shape=(X_train.shape[1],)),
    tf.keras.layers.Dense(256, activation='relu',
        kernel_regularizer=regularizers.l1_l2(l1=0.0001, l2=0.0001)),
    tf.keras.layers.Dense(128, activation='relu',
        kernel_regularizer=regularizers.l1_l2(l1=0.0001, l2=0.0001)),
    tf.keras.layers.Dense(64, activation='relu',
        kernel_regularizer=regularizers.l1_l2(l1=0.001, l2=0.001)),
     tf.keras.layers.Dense(32, activation='relu',
        kernel_regularizer=regularizers.l1_l2(l1=0.01, l2=0.01)),
    tf.keras.layers.BatchNormalization(),
    tf.keras.layers.Dropout(0.4),
    tf.keras.layers.Dense(4, activation='softmax')
  ])

reduce_lr = tf.keras.callbacks.ReduceLROnPlateau(
    monitor='loss', factor=0.001, patience=10, min_lr=1e-6
)

model.compile(optimizer=tf.keras.optimizers.Adam(learning_rate=0.0001), loss='categorical_crossentropy', metrics=['accuracy'])
model.summary()

print("Shape of X_train:", X_train.shape)
print("Shape of y_train:", y_train.shape)

early_stopping = EarlyStopping(monitor='val_loss', patience=10, restore_best_weights=True)

history = model.fit(X_train, y_train,
    validation_data=(X_test, y_test),
    epochs=500,
    batch_size=128, callbacks=[early_stopping,reduce_lr])

train_loss, train_accuracy = model.evaluate(X_train, y_train, verbose=0)
test_loss, test_accuracy = model.evaluate(X_test, y_test, verbose=0)

print(f"Training Loss: {train_loss:.4f}, Training Accuracy: {train_accuracy:.4f}")
print(f"Testing Loss: {test_loss:.4f}, Testing Accuracy: {test_accuracy:.4f}")

# Plot Loss
plt.figure(figsize=(10, 5))
plt.plot(history.history['loss'], label='Training Loss')
plt.plot(history.history['val_loss'], label='Validation Loss')
plt.xlabel('Epochs')
plt.ylabel('Loss')
plt.legend()
plt.title('Training vs Validation Loss')
plt.show()

# Plot Accuracy
plt.figure(figsize=(10, 5))
plt.plot(history.history['accuracy'], label='Training Accuracy')
plt.plot(history.history['val_accuracy'], label='Validation Accuracy')
plt.xlabel('Epochs')
plt.ylabel('Accuracy')
plt.legend()
plt.title('Training vs Validation Accuracy')
plt.show()

pred = model.predict(X_test)
print(pred[:5])

pred = np.argmax(pred, axis=1)
print(pred[:5])

print(y_test[:5])

new_data = {
    'Age': [0.809524],
    'Gender': [0.0],
    'Symptom Severity (1-10)': [0.0],
    'Mood Score (1-10)': [0.0],
    'Sleep Quality (1-10)': [0.4],
    'Physical Activity (hrs/week)': [0.777778],
    'Stress Level (1-10)': [0.0]
}

new_data = pd.DataFrame(new_data)
new_data

y_pred = model.predict(new_data)

print("Hasil Diagnosis: ", y_pred)

# Mapping untuk mendapatkan nama diagnosis
inverse_severity_mapping = {v: k for k, v in severity_mapping.items()}
predicted_classes = y_pred.argmax(axis=1)

# Konversi hasil prediksi numerik menjadi nama diagnosis
predicted_diagnoses = [inverse_severity_mapping[i] for i in predicted_classes]
for id, diagnosis in enumerate(predicted_diagnoses):
    print(f"Sample {id + 1}: {diagnosis}")