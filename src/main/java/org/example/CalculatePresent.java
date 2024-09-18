package org.example;

public interface CalculatePresent {

    // You can create your own calculatePresent
    // func(double f, double r, double n)
    // double p = f / (Math.pow(q + (r/100.0)), n)
    double calculatePresentAnsweredPeople(int Amount_Answered_Users, int Amount_Total_Users);
    double calculatePresentEachQuestion(int count, int total_answers);



}
