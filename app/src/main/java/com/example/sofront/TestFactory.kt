package com.example.sofront


class TestFactory {
    companion object{
        val maxPlanSize = 10
        val makerUid = "작성자 uid"
        val likeNum = 99
        val commentNum = 99
        val downLoadNum = 99
        fun getSomePlan() : Plan{
            val routineList = ArrayList<Routine>()
            val randomSize = getRandomNum(maxPlanSize)
            for(i in 0..randomSize){
                routineList.add(getSomeRoutine())
            }
            return Plan("somePlanName$randomSize",getSomeHashTagList(),routineList,makerUid,true,likeNum,commentNum,downLoadNum)
        }
        fun getSomeHashTagList() : ArrayList<String>{
            val hashTag = ArrayList<String>()
            for(i in 0..getRandomNum(4)){
                hashTag.add("해시태그 "+ getRandomNum(5))
            }
            return hashTag
        }
        fun getRandomNum(maxNum : Int) : Int{
            return (Math.floor(Math.random() * maxNum) + 1).toInt()
        }
        fun getSomeRoutine() : Routine{
            val workoutList = ArrayList<Workout>()
            for(i in 0..getRandomNum(6)){
                val randomSetNum = getRandomNum(5)
                workoutList.add(Workout("WorkoutName" + getRandomNum(6),randomSetNum,getSomeSetList(randomSetNum)))
            }
            return Routine(true,workoutList)
        }
        fun getSomeSetList(setNum : Int) : ArrayList<Set>{
            val set = ArrayList<Set>()
            for(i in 0..setNum){
                set.add(Set(getRandomNum(100), getRandomNum(100)))
            }
            return set
        }
    }
}