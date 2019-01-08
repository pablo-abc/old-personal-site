(ns ^:figwheel-no-load berganzapablo.dev
  (:require
    [berganzapablo.core :as core]
    [devtools.core :as devtools]))

(devtools/install!)

(enable-console-print!)

(core/init!)
