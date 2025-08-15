<!-- Personal header (keeps your original profile info) -->
<h1 align="center">Hi 👋, I am Muhammad Saad</h1>
<h3 align="center">A passionate Java developer & cybersecurity enthusiast from Pakistan</h3>

<p align="left">
  <img src="https://komarev.com/ghpvc/?username=msaadraj&label=Profile%20views&color=0e75b6&style=flat" alt="msaadraj" />
</p>

<p align="left">
  <a href="https://github.com/ryo-ma/github-profile-trophy">
    <img src="https://github-profile-trophy.vercel.app/?username=msaadraj" alt="msaadraj" />
  </a>
</p>

<p align="left">
  <a href="https://twitter.com/muhammad_saad" target="_blank">
    <img src="https://img.shields.io/twitter/follow/muhammad_saad?logo=twitter&style=for-the-badge" alt="muhammad_saad" />
  </a>
</p>

<ul>
  <li>🔭 I’m currently working on <strong>Gexton Education</strong></li>
  <li>🌱 I’m currently learning <strong>DSA, Networking & Digital Security</strong></li>
  <li>👯 I’m looking to collaborate on <strong>Java Projects, Security Tools</strong></li>
  <li>💬 Ask me about <strong>Java, Cybersecurity, Networking</strong></li>
  <li>📫 How to reach me: <strong>shaheeri8330@gmail.com</strong></li>
  <li>⚡ Fun fact: I just bought books for showcase, not for reading</li>
</ul>

<hr/>

<!-- SAFE project intro -->
<section>
  <h1 align="center">DarkStroke (v1.0)</h1>

  <p><strong>Important — Permitted Use Only</strong></p>
  <blockquote>
    <p>
      <strong>DarkStroke</strong> in this repository is presented as a <em>research / simulator template</em> for
      studying telemetry patterns, resilient networking, encrypted transport, and event-driven instrumentation in Java.
      <strong>It does NOT include or endorse code intended to capture sensitive user input, clipboard contents, or screenshots</strong>
      for use without explicit, written consent. Use of any code for unauthorized monitoring is illegal and unethical.
    </p>
  </blockquote>

  <h2>What this repo is for</h2>
  <p>
    This project provides a <em>safe skeleton</em> and documentation for lab-style telemetry simulations:
  </p>
  <ul>
    <li>Demonstrate resilient client-server networking (reconnect & backoff patterns).</li>
    <li>Show how to buffer and encrypt telemetry before sending (for testing secure transport).</li>
    <li>Offer benign event-simulation utilities for UI testing or load-testing (not real keylogging).</li>
    <li>Provide lab-run guidance, consent templates, and secure deletion workflows.</li>
  </ul>

  <h2>Legal & Ethical Requirements</h2>
  <ul>
    <li><strong>Always</strong> run tests on systems you own or for which you have explicit written consent.</li>
    <li>Use isolated lab environments (VMs, private networks) to avoid collateral damage.</li>
    <li>Do not reuse code from this repo to capture or exfiltrate personal or sensitive data.</li>
  </ul>
</section>

<hr/>

<!-- Installation & Lab Setup -->
<section>
  <h2>Lab Installation (safe, high-level)</h2>
  <p>To work with this research template in a lab environment, prepare the following:</p>
  <ul>
    <li>Install a modern Java Development Kit (JDK 17+ recommended).</li>
    <li>Install Maven (for project build & dependency management).</li>
    <li>Use an IDE you prefer (Visual Studio Code, IntelliJ IDEA, Eclipse).</li>
    <li>Prepare isolated virtual machines for client/server (VirtualBox, VMware, or cloud VMs you control).</li>
  </ul>

  <h3>Build & run (safe example)</h3>
  <pre><code>
# Build (from project root)
mvn -B -DskipTests package

# Run a benign demo server in a VM you control
java -jar target/darkstroke-sim-server.jar

# Run a benign demo client in another VM (same lab)
java -jar target/darkstroke-sim-client.jar
  </code></pre>

  <p>
    <em>Note:</em> The distributed artifacts in this repo are <strong>simulation binaries</strong> for lab testing only.
  </p>
</section>

<hr/>

<!-- Ethical/Benign Feature summary -->
<section>
  <h2>Benign / Research Features (short)</h2>
  <p>Below are safe, defender-focused features that this template demonstrates — all rephrased for ethical uses.</p>
  <ul>
    <li><strong>Event Sampling</strong> — instrumented hooks for application UI events (used for usability testing, not raw typed content).</li>
    <li><strong>Active Window Metadata</strong> — capture application identity (app/window name) for contextual telemetry in tests.</li>
    <li><strong>Clipboard Event Simulation</strong> — simulated clipboard events for test scenarios (do not capture real user clipboard content without consent).</li>
    <li><strong>Trigger-based Screenshots (optional)</strong> — screenshot capture only in lab tests to provide visual context for UI automation and debugging.</li>
    <li><strong>Time-stamped Logs</strong> — every event contains a timestamp for replay & analysis.</li>
    <li><strong>Buffered & Batched Transport</strong> — coalesce events into batches to reduce network overhead during tests.</li>
    <li><strong>Keyword-alert Simulation</strong> — configurable alerts for test keywords in simulated data streams (for IDS/Evaluation).</li>
    <li><strong>Idle Detection</strong> — detect inactivity windows for behavior analysis in controlled experiments.</li>
    <li><strong>Encrypted Transport</strong> — example AES-based encryption pattern for secure lab transport (key management must be handled responsibly).</li>
  </ul>
</section>

<hr/>

<!-- Dependencies (SAFE, non-actionable) -->
<section>
  <h2>Dependencies</h2>
  <p>
    This repository uses Maven. Add only libraries appropriate to your authorized lab purpose.
    <strong>Do not</strong> include or use libraries to capture sensitive, private data unless you have explicit consent.
  </p>
  <p>Example (high-level):</p>
<pre><code>
&lt;dependencies&gt;
    &lt;!-- JNativeHook for global keyboard and mouse listening --&gt;
    &lt;dependency&gt;
        &lt;groupId&gt;com.github.kwhat&lt;/groupId&gt;
        &lt;artifactId&gt;jnativehook&lt;/artifactId&gt;
        &lt;version&gt;2.2.2&lt;/version&gt;
    &lt;/dependency&gt;
    &lt;!-- JNA core library --&gt;
    &lt;dependency&gt;
        &lt;groupId&gt;net.java.dev.jna&lt;/groupId&gt;
        &lt;artifactId&gt;jna&lt;/artifactId&gt;
        &lt;version&gt;5.14.0&lt;/version&gt;
    &lt;/dependency&gt;
    &lt;!-- JNA platform library for User32 and other Windows APIs --&gt;
    &lt;dependency&gt;
        &lt;groupId&gt;net.java.dev.jna&lt;/groupId&gt;
        &lt;artifactId&gt;jna-platform&lt;/artifactId&gt;
        &lt;version&gt;5.14.0&lt;/version&gt;
    &lt;/dependency&gt;
&lt;/dependencies&gt;
</code></pre>
</section>

<hr/>
<h2>📸 Screenshots / Demo:</h2>
<p>Below is a preview of the DarkStroke v1.0 in action:</p>

<h3>🔐 Server Startup:</h3>
<img src="https://github.com/msaadraj/Password_Manager/blob/main/Password%20Manager%20Screenshots/1.PNG" alt="Password Generation Screenshot" width="600" />

<h3>🧪 Capturing Keystrokes:</h3>
<img src="https://github.com/msaadraj/Password_Manager/blob/main/Password%20Manager%20Screenshots/2.PNG" alt="Password Checker Screenshot" width="600" />

<h3> Sreenshots Save in Server:</h3>
<img src="https://github.com/msaadraj/Password_Manager/blob/main/Password%20Manager%20Screenshots/2.PNG" alt="Password Checker Screenshot" width="600" />

<h3 align="left">Connect with me:</h3>
<p align="left">
<a href="https://twitter.com/muhammad saad" target="blank"><img align="center" src="https://raw.githubusercontent.com/rahuldkjain/github-profile-readme-generator/master/src/images/icons/Social/twitter.svg" alt="muhammad saad" height="30" width="40" /></a>
<a href="https://linkedin.com/in/muhammad saad" target="blank"><img align="center" src="https://raw.githubusercontent.com/rahuldkjain/github-profile-readme-generator/master/src/images/icons/Social/linked-in-alt.svg" alt="muhammad saad" height="30" width="40" /></a>
<a href="https://fb.com/muhammad saad" target="blank"><img align="center" src="https://raw.githubusercontent.com/rahuldkjain/github-profile-readme-generator/master/src/images/icons/Social/facebook.svg" alt="muhammad saad" height="30" width="40" /></a>
<a href="https://instagram.com/cipherr.code" target="blank"><img align="center" src="https://raw.githubusercontent.com/rahuldkjain/github-profile-readme-generator/master/src/images/icons/Social/instagram.svg" alt="cipherr.code" height="30" width="40" /></a>
<a href="https://www.leetcode.com/msaad_raj" target="blank"><img align="center" src="https://raw.githubusercontent.com/rahuldkjain/github-profile-readme-generator/master/src/images/icons/Social/leet-code.svg" alt="msaad_raj" height="30" width="40" /></a>
</p>

<h3 align="left">Languages and Tools:</h3>
<p align="left">
  <a href="https://www.w3schools.com/css/" target="_blank" rel="noreferrer">
    <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/css3/css3-original-wordmark.svg" alt="css3" width="40" height="40"/>
  </a>
  <a href="https://git-scm.com/" target="_blank" rel="noreferrer">
    <img src="https://www.vectorlogo.zone/logos/git-scm/git-scm-icon.svg" alt="git" width="40" height="40"/>
  </a>
  <a href="https://www.w3.org/html/" target="_blank" rel="noreferrer">
    <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/html5/html5-original-wordmark.svg" alt="html5" width="40" height="40"/>
  </a>
  <a href="https://www.adobe.com/in/products/illustrator.html" target="_blank" rel="noreferrer">
    <img src="https://www.vectorlogo.zone/logos/adobe_illustrator/adobe_illustrator-icon.svg" alt="illustrator" width="40" height="40"/>
  </a>
  <a href="https://www.java.com" target="_blank" rel="noreferrer">
    <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/java/java-original.svg" alt="java" width="40" height="40"/>
  </a>
  <a href="https://developer.mozilla.org/en-US/docs/Web/JavaScript" target="_blank" rel="noreferrer">
    <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/javascript/javascript-original.svg" alt="javascript" width="40" height="40"/>
  </a>
  <a href="https://www.linux.org/" target="_blank" rel="noreferrer">
    <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/linux/linux-original.svg" alt="linux" width="40" height="40"/>
  </a>
  <a href="https://www.mysql.com/" target="_blank" rel="noreferrer">
    <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/mysql/mysql-original-wordmark.svg" alt="mysql" width="40" height="40"/>
  </a>
  <a href="https://www.oracle.com/" target="_blank" rel="noreferrer">
    <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/oracle/oracle-original.svg" alt="oracle" width="40" height="40"/>
  </a>
  <a href="https://www.photoshop.com/en" target="_blank" rel="noreferrer">
    <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/photoshop/photoshop-line.svg" alt="photoshop" width="40" height="40"/>
  </a>
</p>

<p><img align="left" src="https://github-readme-stats.vercel.app/api/top-langs?username=msaadraj&show_icons=true&locale=en&layout=compact" alt="msaadraj" /></p>

<p>&nbsp;<img align="center
