# SSL to work with a secure connection

If your application requires users to enter sensitive data, you should use a secure connection when you send data between the client and the server.

- Secure Sockets Layer(SSL) is a protocol that lets you transfer data between the server and the client over a secure connection. If you're using https rather than http, then you're transmitting data over a secure connection. Additionally, a small lock icon typically appears to the left of the URL.

Due to time it takes to encrypt and decrypt the data that's sent across a secure connection, secure connections are slower than regular HTTP connections.

## How SSL authentication works

To use SSL to transmit data, the client and the server must provide authentication. Both can accept or reject the secure connection.Before secure connection is established, the server uses SSL server authentication to authenticate by providing a digital secure certificate to the browser. Browsers accept certificates that come from trusted sources.

Sometimes, a server may want the client to authenticate itself with SSL client authentication.  For this type of authentication, a digital certificate must be installed on the client browser. The user can typically view the certificate by clicking on the lock icon that's displayed by the browser.

These digital secure certificate come from a trusted source. These certification authorities verify that the person or company requesting the certificate is a valid person or company by checking with a registration authority.

- 40 - bit SSL strength and 56 - bit SSL strength (in the early days of the web)
- 128-bit or 256-bit SSL strength - now

After buying the certificate, it is sent to web host who installs it for your site. Then you can use SSL to transmit data over a secure connection.

SSL strength refers to the length of the generated key that is created during the encryption process.The longer, the more difficult to crack the encryption code. If the web browser isn't able to support the strength provided by the certificate, a lesser strength is used.

The Java Secure Socket Extension(JSSE) API makes it possible for a Java application to use a secure connection.

## [Website security](security.md)
