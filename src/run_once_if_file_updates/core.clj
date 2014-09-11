(ns run-once-if-file-updates.core
  (:use [clojure.java.shell :only [sh]])
  (:gen-class))

(def targets (atom {}))

(defn parse-task [line]
  (let [[file & task] (clojure.string/split line #" ")]
    [file (clojure.string/join " " task)]))

(def start-time (.getTime (java.util.Date.)))

(defn parse-tasks [lines]
  (->> (clojure.string/split lines #"\n")
       (map parse-task)
       (into {})))

(defn run-task [line]
  (apply sh (clojure.string/split line #" ")))

(defn register-task! [filename task-line]
  (println (str "Registered task " (count @targets) ": "
                filename " => " task-line))
  (swap! targets assoc filename task-line))

(defn unregister-task! [filename]
  (println (str "Unregistered task: "
                filename " => " (get @targets filename)))
  (swap! targets dissoc filename))

(defn updated? [filename]
  (> (.lastModified (java.io.File. filename))
     start-time))

(defn exists? [filename]
  (.exists (java.io.File. filename)))

(def ^:dynamic *interval* 10000)

(defn -main [& args]
  ;; initialization
  (let [lines (slurp *in*)]
    (doseq [[file task] (parse-tasks lines)]
      (register-task! file task)))
  ;; main loop
  (loop []
    (if-not (empty? @targets)
      (do
        (doseq [[filename task] @targets]
          (when (and (exists? filename) (updated? filename))
            (println (run-task task))
            (unregister-task! filename)))
        (Thread/sleep *interval*)
        (recur))))
  (shutdown-agents))
