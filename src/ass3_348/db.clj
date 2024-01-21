(ns ass3-348.db)

;; Function to print each student separately
(defn print-students [students]
  (doseq [student students]
    (println student)))

;; Function to print each course separately
(defn print-courses [courses]
  (doseq [course courses]
    (println course)))

;; Function to print each grade separately
(defn print-grades [grades]
  (doseq [grade grades]
    (println grade)))

(defn get-course-name [course-id courses]
  ; Helper function to get the course name for a given course ID
  (let [course (first (filter #(= course-id (-> % :id str)) courses))]
    (-> course :cournam)))

(defn display-student-record [students courses grades]
  ; Function to display student record
  (print "Enter student ID: ")
  (flush)
  (let [student-id (read-line)]
    (let [student (first (filter #(= student-id (-> % :studID str)) students))]
      (if student
        (do
          (println (str "[" (:studID student) " " (:name student) "]"))
          (let [student-grades (filter #(= student-id (-> % :studID str)) grades)]
            (doseq [grade student-grades]
              (let [course-name (get-course-name (:courseID grade) courses)]
                (println (str "[" course-name (:desc grade) (:semester grade) (:grade grade) "]"))))))
        (println "Student not found.")))))



(def grade-conversion
  ; Map to convert grade letters to numerical values
  {"A+" 4.3, "A" 4, "A-" 3.7, "B+" 3.3, "B" 3, "B-" 2.7, "C+" 2.3, "C" 2, "C-" 1.7, "D+" 1.3, "D" 1, "D-" 0.7, "F" 0})

(defn calculate-gpa [students grades]
  ; Function to calculate GPA
  (print "Enter student ID: ")
  (flush)
  (let [student-id (read-line)]
    (let [student (first (filter #(= student-id (-> % :studID str)) students))]
      (if student
        (let [student-grades (filter #(= student-id (-> % :studID str)) grades)]
          (if (seq student-grades)
            (let [total-credits (reduce (fn [sum grade]
                                          (let [credits-str (-> grade :credits str)]
                                            (if (and credits-str (not (empty? credits-str)))
                                              (+ sum (Integer/parseInt credits-str))
                                              sum)))
                                        0
                                        student-grades)
                  weighted-grade-sum (reduce (fn [sum grade]
                                               (let [credits-str (-> grade :credits str)
                                                     grade-str (:grade grade)]
                                                 (if (and credits-str grade-str (not (empty? credits-str)))
                                                   (+ sum (* (Integer/parseInt credits-str)
                                                             (grade-conversion grade-str)))
                                                   sum)))
                                             0
                                             student-grades)]
              (if (zero? total-credits)
                (println "Total Credits is zero. Cannot calculate GPA.")
                (do
                  (println (str "GPA for student " (:studID student) " - " (:name student)))
                  (println (str "Total Credits: " total-credits))
                  (println (str "GPA: " (/ weighted-grade-sum total-credits)))))
              (println "No grades found for the student.")))
          (println "Student not found."))))))



(defn course-average [courses grades]
  ; Function to calculate course average
  (print "Enter course ID: ")
  (flush)
  (let [course-id (read-line)]
    (let [course (first (filter #(= course-id (-> % :id str)) courses))]
      (if course
        (let [course-grades (filter #(= course-id (-> % :courseID str)) grades)]
          (if (seq course-grades)
            (let [semester-grades (group-by :semester course-grades)
                  average-grades (->> semester-grades
                                      (map (fn [[semester grades]]
                                             (let [num-grades (count grades)
                                                   total-grade-sum (reduce (fn [sum grade]
                                                                             (+ sum (grade-conversion (:grade grade))))
                                                                           0
                                                                           grades)
                                                   average-grade (/ total-grade-sum num-grades)]
                                               [semester average-grade])))
                                      (into {}))]
              (println (str "Course Average for course " (:cournam course) " - " (:courno course)))
              (doseq [[semester average-grade] average-grades]
                (println (str semester " - " average-grade))))
            (println "No grades found for the course.")))
        (println "Course not found.")))))


(defn quit-program []
  (println "Exiting the program...")
  (System/exit 0))