package me.joemiller.groovy

class StatsdClient {
    def host, port, inetAddr
    def sock = new DatagramSocket()
    def RNG = new Random()
    
    StatsdClient( host = 'localhost', port = 8125 ) {
        this.host = host
        this.port = port
        this.inetAddr = InetAddress.getByName( this.host )
    }
        
    // +stats+: a string or array of strings with name of statistic
    // +time+: time to log in ms
    // +samplerate+: a double specifying a sample rate, default = 1 (100%)
    def timing( stats, time, sampleRate = 1 ) {
        def _stats = [stats].flatten()
        sendStats( _stats.collect { String.format("%s:%d|ms", it, time) }, sampleRate )
    }
    
    def increment( stats, delta = 1 , sampleRate = 1 ) {
        updateCounter( stats, delta, sampleRate )
    }
    
    def decrement( stats, delta = -1, sampleRate = 1 ) {
        updateCounter( stats, delta, sampleRate )
    }
    
    def updateCounter( stats, delta = 1, sampleRate = 1 ) {
        def _stats = [stats].flatten()
        sendStats( _stats.collect { "${it}:${delta}|c" }, sampleRate )
    }
    
    // +data+: a string or array of strings
    // +samplerate+: a double specifying a sample rate, default = 1 (100%)
    private def sendStats( data, sampleRate = 1 ) {
        def _data = [data].flatten()
        
        if (sampleRate < 1.0) {
            def sampledData = []
            _data.each { d ->
                if (RNG.nextDouble() <= sampleRate) {
                    sampledData.add( String.format("%s|@%f", d, sampleRate) )
                }
            }
            _data = sampledData
        }
        doSend( _data )
    }
    
    // +data+: a string or array of strings that will be sent to statsd
    private def doSend( data ) {
        def _data = [data].flatten()
        _data.each { d ->
            def bytes = d.getBytes()
            this.sock.send( new DatagramPacket(bytes, bytes.length, this.inetAddr, this.port) )
        }
    }
}
