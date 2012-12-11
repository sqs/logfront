package logfront.controllers

import play.api._
import play.api.mvc._

object Logs extends Controller {
  def get(host: String, logName: String) = Action {
    Ok(
      execRemote(host, "sudo tail -n 1500 /var/log/tomcat7/application.log")
    ).as("text/plain").withHeaders(
      CACHE_CONTROL -> "no-cache"
    )
  }

  private def execRemote(host: String, command: String): String = {
    import java.io.{ File, IOException }
    import java.security.PublicKey
    import java.util.concurrent.TimeUnit
    import net.schmizz.sshj.SSHClient
    import net.schmizz.sshj.common.IOUtils
    import net.schmizz.sshj.connection.channel.direct.Session
    import net.schmizz.sshj.connection.channel.direct.Session.Command
    import net.schmizz.sshj.transport.verification.HostKeyVerifier
    import scala.io.Source

    play.api.Logger.debug(s"Executing command: ssh $host '$command'")

    val keyFile = new File(System.getProperty("user.home"), ".ssh/blendlive.pem").getAbsolutePath

    val ssh = new SSHClient()
    ssh.addHostKeyVerifier(new HostKeyVerifier() {
      def verify(arg0: String, arg1: Int, arg2: PublicKey): Boolean = true
    })

    ssh.connect(host)
    try {
      ssh.authPublickey("ec2-user", keyFile)
      val session = ssh.startSession()
      try {
        session.allocateDefaultPTY()
        val cmd = session.exec(command)
        cmd.join(5, TimeUnit.SECONDS)
        val out = Source.fromInputStream(cmd.getInputStream, "UTF-8").getLines.mkString("\n")
        out
      } finally {
        session.close()
      }
    } finally {
      ssh.disconnect()
    }
  }
}
