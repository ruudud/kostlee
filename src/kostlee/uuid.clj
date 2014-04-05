(ns kostlee.uuid)

(defn uuid [] (str (java.util.UUID/randomUUID)))
