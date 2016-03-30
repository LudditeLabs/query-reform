/** The benchmark is distributed under the Creative Commons,
* Attribution-NonCommercial-NoDerivatives. This license includes the benchmark database
* and its derivatives. For attribution, please cite this page, and our publications below.
* This data is provided free of charge for non-commercial and academic benchmarking and
* experimentation use. If you would like to contribute to the benchmark, please contact us.
* If you believe you intended usage may be restricted by the license,
* please contact us and we can discuss the possibilities.
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.PublicKeyAuthenticationClient;
import com.sshtools.j2ssh.io.IOStreamConnector;
import com.sshtools.j2ssh.io.IOStreamConnectorState;
import com.sshtools.j2ssh.session.SessionChannelClient;
import com.sshtools.j2ssh.transport.publickey.SshPrivateKey;
import com.sshtools.j2ssh.transport.publickey.SshPrivateKeyFile;
import com.sshtools.j2ssh.configuration.ConfigurationLoader;

/**
 * Demonstrates a public key authentication connection to an SSH server.
 *
 * @author <A HREF="mailto:lee@sshtools.com">Lee David Painter</A>
 * @version $Id: PublicKeyConnect.java,v 1.8 2003/07/16 10:42:08 t_magicthize Exp $
 *
 * @created 20 December 2002
 */
public class PublicKeyConnect {

    /**
   * The main program for the PublicKeyConnect class
   *
   * @param args The command line arguments
   */
    public static void main(String args[]) {
        try {
            ConfigurationLoader.initialize(false);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Connect to host? ");
            String hostname = reader.readLine();
            SshClient ssh = new SshClient();
            ssh.connect(hostname);
            PublicKeyAuthenticationClient pk = new PublicKeyAuthenticationClient();
            System.out.print("Username? ");
            String username = reader.readLine();
            pk.setUsername(username);
            System.out.print("Path to private key file? ");
            String filename = reader.readLine();
            SshPrivateKeyFile file = SshPrivateKeyFile.parse(new File(filename));
            String passphrase = null;
            if (file.isPassphraseProtected()) {
                System.out.print("Enter passphrase? ");
                passphrase = reader.readLine();
            }
            SshPrivateKey key = file.toPrivateKey(passphrase);
            pk.setKey(key);
            int result = ssh.authenticate(pk);
            if (result == AuthenticationProtocolState.COMPLETE) {
                SessionChannelClient session = ssh.openSessionChannel();
                if (!session.requestPseudoTerminal("vt100", 80, 24, 0, 0, "")) System.out.println("Failed to allocate a pseudo terminal");
                if (session.startShell()) {
                    InputStream in = session.getInputStream();
                    OutputStream out = session.getOutputStream();
                    IOStreamConnector input = new IOStreamConnector(System.in, session.getOutputStream());
                    IOStreamConnector output = new IOStreamConnector(session.getInputStream(), System.out);
                    output.getState().waitForState(IOStreamConnectorState.CLOSED);
                } else System.out.println("Failed to start the users shell");
                ssh.disconnect();
            }
            if (result == AuthenticationProtocolState.PARTIAL) {
                System.out.println("Further authentication requried!");
            }
            if (result == AuthenticationProtocolState.FAILED) {
                System.out.println("Authentication failed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
