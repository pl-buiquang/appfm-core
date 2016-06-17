/**
 * Created by buiquang on 9/7/15.
 */
package fr.limsi.iles.cpm.utils

import java.io.{File, FileInputStream}
import java.util.function.BiConsumer

import com.typesafe.scalalogging.LazyLogging
import org.yaml.snakeyaml.Yaml

object ConfManager extends LazyLogging{
  val defaultDockerBaseImage = "base_cpm_shell"
  val moduleDefinitionDirectory = "_DEF_DIR"
  val runWorkingDirectory = "_RUN_DIR"

  var confMap : java.util.Map[String,Any] = null
  val defaultConfFile = "/conf.yml"
  var confPath = defaultConfFile

  def get(key:String)={
    if(confMap==null){
      init()
    }
    if(confMap.containsKey(key)){
      confMap.get(key)
    }else{
      throw new Exception(key+" was not found in configuration!")
    }
  }

  /**
   * Initialize ConfManager from a yaml file, default to conf.yml
   * @param confPath
   */
  def init(confPath:String) :Unit={
    val conffile = getClass.getResource(confPath)
    this.confPath = confPath

    var filename : String = null
    if(conffile == null){
      filename = confPath
    }else{
      filename = conffile.getFile;
    }
    val file = new File(filename)
    if(file.exists()){
      confMap = Utils.yamlTabFixLoad(filename)
    }else{
      val input :java.io.InputStream = getClass.getResourceAsStream(confPath); // when in jar
      val yaml = new Yaml()
      confMap = yaml.load(input).asInstanceOf[java.util.Map[String, Any]]
    }


  }

  def init() :Unit= {
    init(defaultConfFile)
  }

  def reload()={
    val oldConf = confMap
    init(this.confPath)
    val noNeed2RestartItems = List[String]("corpus_dir","modules_dir")
    var restartRequired = false
    confMap.forEach(new BiConsumer[String,Any] {
      override def accept(t: String, u: Any): Unit = {
        if(u.toString != oldConf.get(t).toString && !noNeed2RestartItems.exists(_==t)){
          restartRequired = true
        }
      }
    })
    if(restartRequired){
      this.confMap = oldConf
      logger.error("Configuration not changed, you must restart AppFM to apply theses changes (only corpus_dir, modules_dir can be changed at runtime)")
    }
  }
}
