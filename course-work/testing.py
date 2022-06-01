from main import Main

main = Main()
main.execute_model()
filenames = ["UrbanSound8K/test_audio/dog_bark_1.wav", "UrbanSound8K/test_audio/drill_1.wav",
             "UrbanSound8K/test_audio/gun_shot_1.wav", "UrbanSound8K/test_audio/child_1.wav",
             "UrbanSound8K/test_audio/street_music_1.wav", "UrbanSound8K/test_audio/car_horn_1.wav",
             "UrbanSound8K/test_audio/drill_2.wav", "UrbanSound8K/test_audio/air_conditioner_1.wav",
             "UrbanSound8K/test_audio/jackhammer_1.wav", "UrbanSound8K/test_audio/siren_1.wav"]
print('Unique classes: \n', main.unique_classification)
for filename in filenames:
    prediction_class = main.predict_audio(filename)
    print('Prediction for file - {} is: '.format(filename), prediction_class)
# main.eval_model_performance()
