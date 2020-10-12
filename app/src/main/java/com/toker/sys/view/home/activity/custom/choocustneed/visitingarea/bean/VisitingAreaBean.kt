package com.toker.sys.view.home.activity.custom.choocustneed.visitingarea.bean

class VisitingAreaBean( val classify: List<Classify>) {
    class Classify(var title: String,internal var des: List<Des>) {

        class Des(internal var des: String) {
            internal var isSelect: Boolean = false
        }

    }
}