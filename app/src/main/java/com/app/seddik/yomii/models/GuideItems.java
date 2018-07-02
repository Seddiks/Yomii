package com.app.seddik.yomii.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Seddik on 10/06/2018.
 */

public class GuideItems  implements Serializable{
    private ArrayList<Data> data;
    private String  message;
    private boolean success;

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


    public static class Data implements Serializable {
        private int guide_id;
        private String photo_path, location, title_guide, experience, history, budget_advice, best_time_to_visit, best_place_to_visit,
                restaurant_suggestions, transportation_advice, language, other_informations, pathPhoto_from_uri;

        public int getGuide_id() {
            return guide_id;
        }

        public void setGuide_id(int guide_id) {
            this.guide_id = guide_id;
        }

        public String getPhoto_path() {
            return photo_path;
        }

        public void setPhoto_path(String photo_path) {
            this.photo_path = photo_path;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getTitle_guide() {
            return title_guide;
        }

        public void setTitle_guide(String title_guide) {
            this.title_guide = title_guide;
        }

        public String getExperience() {
            return experience;
        }

        public void setExperience(String experience) {
            this.experience = experience;
        }

        public String getHistory() {
            return history;
        }

        public void setHistory(String history) {
            this.history = history;
        }

        public String getBudget_advice() {
            return budget_advice;
        }

        public void setBudget_advice(String budget_advice) {
            this.budget_advice = budget_advice;
        }

        public String getBest_time_to_visit() {
            return best_time_to_visit;
        }

        public void setBest_time_to_visit(String best_time_to_visit) {
            this.best_time_to_visit = best_time_to_visit;
        }

        public String getBest_place_to_visit() {
            return best_place_to_visit;
        }

        public void setBest_place_to_visit(String best_place_to_visit) {
            this.best_place_to_visit = best_place_to_visit;
        }

        public String getRestaurant_suggestions() {
            return restaurant_suggestions;
        }

        public void setRestaurant_suggestions(String restaurant_suggestions) {
            this.restaurant_suggestions = restaurant_suggestions;
        }

        public String getTransportation_advice() {
            return transportation_advice;
        }

        public void setTransportation_advice(String transportation_advice) {
            this.transportation_advice = transportation_advice;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getOther_informations() {
            return other_informations;
        }

        public void setOther_informations(String other_informations) {
            this.other_informations = other_informations;
        }

        public String getPathPhoto_from_uri() {
            return pathPhoto_from_uri;
        }

        public void setPathPhoto_from_uri(String pathPhoto_from_uri) {
            this.pathPhoto_from_uri = pathPhoto_from_uri;
        }
    }


    }
