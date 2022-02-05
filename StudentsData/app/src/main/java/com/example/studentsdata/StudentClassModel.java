package com.example.studentsdata;

public class StudentClassModel {

    String fileName,studentName,studentRollNo;

    public StudentClassModel(){}

    public StudentClassModel(String fileName, String studentName, String studentRollNo) {
        this.fileName = fileName;
        this.studentName = studentName;
        this.studentRollNo = studentRollNo;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentRollNo() {
        return studentRollNo;
    }

    public void setStudentRollNo(String studentRollNo) {
        this.studentRollNo = studentRollNo;
    }
}
