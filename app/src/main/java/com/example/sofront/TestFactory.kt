package com.example.sofront

import java.util.*
import kotlin.collections.ArrayList


class TestFactory {
    companion object{
        private val maxPlanSize = 10
        private val makerUid = "작성자 uid"
        private val likeNum = 99
        private val commentNum = 99
        private val downLoadNum = 99
        fun getSomePlan(size : Int) : ArrayList<Plan>{
            val planList = ArrayList<Plan>()
            for(i in 0..size){
                planList.add(getSomePlan())
            }
            return planList
        }
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
        fun getSomeRoutine(size : Int) : ArrayList<Routine>{
            val routineList = ArrayList<Routine>()
            for(i in 0..size){
                routineList.add(getSomeRoutine())
            }
            return routineList
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
        fun getSomePortfolio(size : Int) : ArrayList<Portfolio>{
            val portfolioList = ArrayList<Portfolio>()
            for(i in 0..size){
                portfolioList.add(getSomePortfolio())
            }
            return portfolioList
        }
        fun getSomePortfolio() : Portfolio{
            val randomNum = getRandomNum(10)
            return Portfolio(
                getRandomNum(100),"PortfolioTitle$randomNum","PortfolioWriter$randomNum","content hmmm $randomNum",getSomeDate(),
                getSomeHashTagList(),
                getRandomNum(100),
                getRandomNum(100))
        }
        fun getSomeDate() : String{
            return "2022-${getRandomNum(12)}-${ getRandomNum(31)}-${getRandomNum(24)}-${getRandomNum(60)}"
        }
        fun getSomeComment(size: Int) : ArrayList<Comment>{
            val list = ArrayList<Comment>()
            for(i in 0..size){
                list.add(getSomeComment())
            }
            return list
        }
        fun getSomeComment() : Comment{
            return Comment("tmpPlanName","tmpUid","tmpName","2022-08-17","","content")
        }
    }
}