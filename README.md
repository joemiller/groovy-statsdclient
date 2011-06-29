groovy StatsdClient
===================

A groovy client for [StatsD](https://github.com/etsy/statsd), Etsy.com's daemon for aggregating 
statistics (counters and timers), rolling them up, then sending them to [graphite](http://graphite.wikidot.com)


Usage
-----
The usage is very similar to statsd clients in other languages, including support for 1 or more stats
in a single call as well as sampling.  By default, sampling is disabled.
    
    s = new StatsdClient("localhost", 8125)
    
    s.timing( "test.timing", 300 )
    s.timing( "test.timing.sampled", 200, 0.1 )
    s.timing( ["test.first.timing", "test.second.timing"], 100 )
    s.timing( ["test.first.timing.sampled", "test.second.timing.sampled"], 100, 0.5 )

    s.increment( 'increment.by.one' )
    s.increment( 'increment.by.some.value', 20 )
    s.increment( 'increment.by.some.value.and.sampled', 20, 0.5 )
    s.increment( ['increment.by.one.First', 'increment.by.one.Second' ] )
         
    s.increment( 'decrement.by.one' )
    s.increment( 'decrement.by.some.value', -20 )
    s.increment( 'decrement.by.some.value.and.sampled', -20, 0.5 )
    s.increment( ['decrement.by.one.First', 'decrement.by.one.Second' ] )
     
Run the above script:  `groovy -cp groovy-statsdclient.jar  script.groovy`

Building
--------
1. Download and install gradle
2. Fetch the latest code: `git clone git://github.com/joemiller/groovy-statsdclient.git`
3. Run the tests using: `gradle test`
4. Build the jar using: `gradle jar`

You will find the built jar in `./build/libs`.

Todo / Future
-------------
1. Add to maven central. 
2. Once in maven central, document how the lib can be used with maven, gradle and Grape/@Grab

License
-------
Copyright 2011 Joe Miller

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.