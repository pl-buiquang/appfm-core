package fr.limsi.iles.cpm

import fr.limsi.iles.cpm.module.definition.{ModTree, ModNode, ModuleManager}
import fr.limsi.iles.cpm.utils.Utils
import org.json.JSONObject

import scala.sys.process.Process

/**
 * Created by buiquang on 10/7/15.
 */
object ScratchTests extends App{

  class ModuleDef(val desc:String,val inputs:String){

  }

  def search(query:String,modules:Map[String,ModuleDef]):List[ModuleDef]={
    val keywords = query.split("""\s+""")
    modules.foldLeft(List[((Int,Int,Int),ModuleDef)]())((agg,el)=>{
      val name = el._1
      val desc = el._2.desc
      val other = el._2.inputs
      var matchCountName = 0
      var matchCountDesc = 0
      var matchCountOther = 0
      keywords.foreach((word)=>{
        var offset : Int= 0
        var index = -1
        while ({index = name.substring(offset).toLowerCase.indexOf(word.toLowerCase); index} != -1){
          matchCountName += 1
          offset += index+word.length
        }
        offset = 0
        index = -1
        while ({index = desc.substring(offset).toLowerCase.indexOf(word.toLowerCase); index} != -1){
          matchCountDesc += 1
          offset += index+word.length
        }
        offset = 0
        index = -1
        while ({index = other.substring(offset).toLowerCase.indexOf(word.toLowerCase); index} != -1){
          matchCountOther += 1
          offset += index+word.length
        }
      })
      ((matchCountName,matchCountDesc,matchCountOther),el._2) :: agg
    }).filter((el)=>{
      el._1._1 > 0 || el._1._2 > 0 || el._1._3 > 0
    }).sortWith((a,b)=>{
      a._1._1 > b._1._1 || (a._1._1 == b._1._1 && a._1._2 > b._1._2 || (a._1._2 == b._1._2 && a._1._3 > b._1._3))
    }).map((el)=>{
      el._2
    })
  }



  override def main (args: Array[String]) {
    val m2 = new ModuleDef("shit","module")
    val m3 = new ModuleDef("module module module","test")
    val m4 = new ModuleDef("no","yes")
    val m1 = new ModuleDef("module","test2")
    val m0 = new ModuleDef("module","test")

    val modules = Map("module"->m0,"sa"->m1,"sdf"->m2,"m3"->m3,"mm4"->m4)
    search("module",modules)
  }

}
