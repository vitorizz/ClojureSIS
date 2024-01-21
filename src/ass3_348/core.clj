(ns ass3-348.core
  (:require [ass3-348.db :as db]
            [ass3-348.menu :as menu]
            [clojure.spec.alpha :as s]
            [clojure.string :as str]))


;; STUDENTS

;; Define the keywords for the schema
(s/def ::studID string?)
(s/def ::name string?)
(s/def ::address string?)
(s/def ::phoneNumber string?)

;; Define the student schema
(def Student
  (s/cat :studID ::studID
         :name ::name
         :address ::address
         :phoneNumber ::phoneNumber))

;; Retrieve the contents from the text file as a whole string
(def fileStudents (slurp "studs.txt"))

;; Split the lines and the '|', then map each aspect of the student to a collection
(def lines (str/split-lines fileStudents))
(def students (map #(str/split % #"\|") lines))

;; Conform each student record to the defined schema
(def structuredStudents (map #(s/conform Student %) students))




;; COURSES

;; Define the keywords for the schema
(s/def ::id string?)
(s/def ::cournam string?)
(s/def ::courno string?)
(s/def ::credits string?)
(s/def ::desc string?)

;; Define the course schema
(def Course
  (s/cat :id ::id
         :cournam ::cournam
         :courno ::courno
         :credits ::credits
         :desc ::desc))

;; Retrieve the contents from the text file as a whole string
(def fileCourse (slurp "courses.txt"))

;; Split the lines and the '|', then map each aspect of the course to a collection
(def line (str/split-lines fileCourse))
(def courses (map #(str/split % #"\|") line))

;; Conform each course record to the defined schema
(def structuredCourses (map #(s/conform Course %) courses))




;; GRADES

;; Define the keywords for the schema
(s/def ::studID string?)
(s/def ::courseID string?)
(s/def ::semester string?)
(s/def ::grade string?)

;; Define the grades schema
(def Grade
  (s/cat :studID ::studID
         :courseID ::courseID
         :semester ::semester
         :grade ::grade))

;; Retrieve the contents from the text file as a whole string
(def fileContents (slurp "grades.txt"))

;; Split the lines and the '|', then map each aspect of the grade to a collection
(def l (str/split-lines fileContents))
(def grades (map #(str/split % #"\|") l))

;; Conform each grade record to the defined schema
(def structuredGrades (map #(s/conform Grade %) grades))


(defn display-students []
  ; Function to display students
  (let [students (doall structuredStudents)]
    (db/print-students students)))

(defn display-courses []
  ; Function to display courses
  (let [courses (doall structuredCourses)]
    (db/print-courses courses)))

(defn display-grades []
  ; Function to display grades
  (let [grades (doall structuredGrades)]
    (db/print-grades grades)))

(defn student-record []
  ; Function to display student record 
  (let [students (doall structuredStudents)
        courses (doall structuredCourses)
        grades (doall structuredGrades)]
    (db/display-student-record students courses grades)))

(defn calculate-gpa []
  ; Function to calculate GPA
  (let [students (doall structuredStudents)
        grades (doall structuredGrades)]
    (db/calculate-gpa students grades)))

(defn course-average []
  ; Function to calculate course average
  (let [courses (doall structuredCourses)
        grades (doall structuredGrades)]
    (db/course-average courses grades)))



(defn main-menu []
  ; Main menu function
  (loop []
    ;;if wanna put this text in menu, put just the writing and call the text 
    (menu/menu_printer)
    (let [option (read-line)]
      (case option
        "1" (do
              (display-courses)
              (recur))
        "2" (do
              (display-students)
              (recur))
        "3" (do
              (display-grades)
              (recur))
        "4" (do
              (student-record)
              (recur))
        "5" (do
              (calculate-gpa)
              (recur))
        "6" (do
              (course-average)
              (recur))
        "7" (db/quit-program)
        (do
          (println "Invalid option, please try again.")
          (recur))))))


(defn -main [& args]
  (main-menu) ; Call the main menu function to start the program
  )
