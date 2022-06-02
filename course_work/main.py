import pandas as pd
import os
import librosa
import numpy as np

from tqdm import tqdm
from sklearn.preprocessing import LabelEncoder
from sklearn.model_selection import train_test_split
from datetime import datetime

import tensorflow as tf
from keras.models import Sequential, load_model
from keras.layers import Dense, Dropout, Activation
from keras.utils import to_categorical
from keras.optimizers import Adam
from keras.callbacks import ModelCheckpoint
from sklearn import metrics

import matplotlib.pyplot as plt
import seaborn as sns


class Main:

    def __init__(self):
        self.audio_dataset_path = 'UrbanSound8K/audio'
        self.metadata = pd.read_csv('UrbanSound8K/metadata/UrbanSound8K.csv')
        self.label_encoder = LabelEncoder()
        self.extracted_features = []
        self.test_features = []
        self.extracted_features_df = []
        self.test_features_df = []
        self.unique_classification = []
        self.X = []
        self.y = []
        self.X_train = 0
        self.y_train = 0
        self.X_test = 0
        self.y_test = 0

        if os.path.exists('saved_models/audio_classification.hdf5'):
            self.model = load_model('saved_models/audio_classification.hdf5')
        else:
            self.model = 0
        self.preprocess_data()

    def features_extractor(self, file):
        audio, sample_rate = librosa.load(file, res_type='kaiser_fast')
        mfccs_features = librosa.feature.mfcc(y=audio, sr=sample_rate, n_mfcc=40)

        mfccs_scaled_features = np.mean(mfccs_features.T, axis=0)

        return mfccs_scaled_features

    def preprocess_data(self):
        if not os.stat('extracted_features.txt').st_size:
            with open('extracted_features.txt', 'wb') as data_file:
                for index_num, row in tqdm(self.metadata.iterrows()):

                    file_name = os.path.join(os.path.abspath(self.audio_dataset_path),
                                             'fold' + str(row["fold"]) + '/', str(row["slice_file_name"]))
                    final_class_labels = row['class']
                    data = self.features_extractor(file_name)
                    if row["fold"] != 10:
                        self.extracted_features.append([data, final_class_labels])
                    else:
                        self.test_features.append([data, final_class_labels])

                self.extracted_features = np.array(self.extracted_features, dtype=object)
                self.test_features = np.array(self.test_features, dtype=object)
                with open('test_features.txt', 'wb') as test_data_file:
                    np.save(test_data_file, self.test_features)
                test_data_file.close()
                np.save(data_file, self.extracted_features)
            data_file.close()
        else:
            self.extracted_features = np.load("extracted_features.txt", encoding='bytes', allow_pickle=True)
            self.test_features = np.load("test_features.txt", encoding='bytes', allow_pickle=True)
        self.convert_extracted_features()

    def convert_extracted_features(self):
        self.extracted_features_df = pd.DataFrame(self.extracted_features, columns=['feature', 'class'])
        self.test_features_df = pd.DataFrame(self.test_features, columns=['feature', 'class'])
        self.X = np.array(self.extracted_features_df['feature'].tolist())
        self.y = np.array(self.extracted_features_df['class'].tolist())
        self.unique_classification = np.unique(self.y)
        self.y = to_categorical(self.label_encoder.fit_transform(self.y))
        self.X_train, self.X_test, self.y_train, self.y_test = \
            train_test_split(self.X, self.y, test_size=0.2, random_state=0)

    def execute_model(self):
        if not self.model:
            self.model_creation()

        self.model_training()

    def model_creation(self):
        num_labels = self.y.shape[1]

        self.model = Sequential()

        self.model.add(Dense(100, input_shape=(40,)))
        self.model.add(Activation('relu'))
        self.model.add(Dropout(0.5))

        self.model.add(Dense(200))
        self.model.add(Activation('relu'))
        self.model.add(Dropout(0.5))

        self.model.add(Dense(100))
        self.model.add(Activation('relu'))
        self.model.add(Dropout(0.5))

        self.model.add(Dense(num_labels))
        self.model.add(Activation('softmax'))

        self.model.compile(loss='categorical_crossentropy', metrics=['accuracy'], optimizer='adam')

    def model_training(self):
        num_epochs = 100
        num_batch_size = 32
        checkpointer = ModelCheckpoint(filepath='saved_models/audio_classification.hdf5',
                                       verbose=1, save_best_only=True)

        start = datetime.now()
        self.model.fit(self.X_train, self.y_train, batch_size=num_batch_size, epochs=num_epochs,
                       validation_data=(self.X_test, self.y_test), callbacks=[checkpointer])
        duration = datetime.now() - start

        print("Training complete in time: ", duration)

        loss, acc = self.model.evaluate(self.X_test, self.y_test)
        print(f"Loss is {loss},\nAccuracy is {acc * 100}")

    def predict_audio(self, filename):
        prediction_feature = self.features_extractor(filename)
        prediction_feature = prediction_feature.reshape(1, -1)
        predict = np.argmax(self.model.predict(prediction_feature), axis=-1)
        prediction_class = self.label_encoder.inverse_transform(predict)
        return prediction_class[0]

    def eval_model_performance(self):

        features = self.test_features_df['feature'].tolist()
        labels = self.test_features_df['class'].tolist()



        prediction_ids = []
        for feature in features:
            feature = feature.reshape(1, -1)
            predict = np.argmax(self.model.predict(feature), axis=-1)
            print(predict)
            prediction_ids.append(predict)

        labels_ids = []
        for label in labels:
            label_id = tf.math.argmax(label == self.unique_classification)
            labels_ids.append(label_id)

        confusion_matrix = tf.math.confusion_matrix(labels_ids, prediction_ids)
        plt.figure(figsize=(10, 8))
        sns.heatmap(confusion_matrix,
                    xticklabels=self.unique_classification,
                    yticklabels=self.unique_classification,
                    annot=True, fmt='g')
        plt.xlabel('Prediction')
        plt.ylabel('Label')
        plt.show()

    # def test(self):
    #     features = []
    #     for x in self.X_test:
    #
    #         feature = np.argmax(x, axis=-1)
    #         features.append(feature)
    #     print(features)