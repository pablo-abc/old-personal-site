(ns berganzapablo.layout.navbar)

(defn navbar-layout []
  [:nav
   [:a {:href "/"} "Home"] " | "
   [:a {:href "/blog"} "Blog"] " | "
   [:a {:href "/about"} "About me"]])
