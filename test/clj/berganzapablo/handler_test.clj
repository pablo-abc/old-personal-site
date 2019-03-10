(ns berganzapablo.handler-test
  (:use midje.sweet)
  (:require [berganzapablo.handler :as handler])
  (:import [java.io ByteArrayInputStream]))

(fact "head returns an array"
      (handler/head) => #(= (count %) 8)
      (handler/head) => (has-prefix [:head]))

(fact "mount-layout should return array"
      (handler/mount-layout [:div "hi"]) => (has-prefix [:div#app]))

(fact "current-page should return a html"
      (handler/current-page {:uri "/"}) => #"DOCTYPE html")

(fact "index-handler should return a map with an html body"
      (handler/index-handler {:uri "/"}) => (contains
                                            {:status 200
                                             :body #"DOCTYPE html"}))

(fact "keywordize-map should return a map with keywords instead of strings"
      (handler/keywordize-map {"key" "value"}) => {:key "value"})
