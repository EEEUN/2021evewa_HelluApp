package com.example.HelluApp.Metaverse;

public class metaverse_note {
    int _id;
    String trainingClass;
    String trainerName;

    public metaverse_note(int _id, String trainingClass, String trainerName){
        this._id = _id;
        this.trainingClass = trainingClass;
        this.trainerName = trainerName;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTrainingClass() {
        return trainingClass;
    }

    public void setTrainingClass(String trainingClass) {
        this.trainingClass = trainingClass;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }
}
