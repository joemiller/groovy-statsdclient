import groovy.util.GroovyTestCase
import groovy.mock.interceptor.StubFor
import me.joemiller.groovy.StatsdClient

class StatsdClientMethodTests extends GroovyTestCase {
    def resultText
    def subject
    def mockRNG
    
    void setUp() {
        subject = new StatsdClient()
        subject.metaClass.doSend = { resultText = it }

        mockRNG = new Random()
        mockRNG.metaClass.nextDouble = { 0 } // not so random!
        subject.RNG = mockRNG
    }
    
    // test initialization
    void test_initialization_without_parameters() {
        def s = new StatsdClient()
        assert s.host == 'localhost'
        assert s.port == 8125
    }
    
    void test_initialization_with_parameters() {
        def s = new StatsdClient('localhost', 3000)
        assert s.host == 'localhost'
        assert s.port == 3000
    }
    
    // timing() tests
    void test_timing_with_full_samplerate() {
        subject.timing('test.timing', 100)
        assert resultText == ["test.timing:100|ms"]
    }
    
    void test_timing_with_partial_samplerate() {
        subject.timing('test.timing', 100, 0.5)
        assert resultText == ["test.timing:100|ms|@0.500000"]
    }

    void test_timing_with_multipe_stats_and_full_samplerate() {
        subject.timing( ['test.timing', 'test2.timing'], 100)
        assert resultText == ["test.timing:100|ms", "test2.timing:100|ms"]
    }
    
    // increment() tests
    void test_increment_with_single_stat_and_no_arguments() {
        subject.increment('test.incr')
        assert resultText == ['test.incr:1|c']
    }
    
    void test_increment_with_single_stat_and_explicit_value() {
        subject.increment('test.incr', 100)
        assert resultText == ['test.incr:100|c']
    }

    void test_increment_with_single_stat_and_explicit_value_and_partial_samplerate() {
        subject.increment('test.incr', 100, 0.5)
        assert resultText == ['test.incr:100|c|@0.500000']
    }
    
    void test_increment_with_multiple_stats_and_no_arguments() {
        subject.increment( ['test.incr', 'test2.incr'] )
        assert resultText == ['test.incr:1|c', 'test2.incr:1|c']
    }
    
    void test_increment_with_multiple_stats_and_explicit_value() {
        subject.increment( ['test.incr', 'test2.incr'], 100 )
        assert resultText == ['test.incr:100|c', 'test2.incr:100|c']
    }

    void test_increment_with_multiple_stats_and_explicit_value_and_partial_samplerate() {
        subject.increment( ['test.incr', 'test2.incr'], 100, 0.5 )
        assert resultText == ['test.incr:100|c|@0.500000', 'test2.incr:100|c|@0.500000']
    }

    // decrement() tests
    void test_decrement_with_single_stat_and_no_arguments() {
        subject.decrement('test.decr')
        assert resultText == ['test.decr:-1|c']
    }
    
    void test_decrement_with_single_stat_and_explicit_value() {
        subject.decrement('test.decr', -100)
        assert resultText == ['test.decr:-100|c']
    }

    void test_decrement_with_single_stat_and_explicit_value_and_partial_samplerate() {
        subject.decrement('test.decr', -100, 0.5)
        assert resultText == ['test.decr:-100|c|@0.500000']
    }
    
    void test_decrement_with_multiple_stats_and_no_arguments() {
        subject.decrement( ['test.decr', 'test2.decr'] )
        assert resultText == ['test.decr:-1|c', 'test2.decr:-1|c']
    }
    
    void test_decrement_with_multiple_stats_and_explicit_value() {
        subject.decrement( ['test.decr', 'test2.decr'], -100 )
        assert resultText == ['test.decr:-100|c', 'test2.decr:-100|c']
    }

    void test_decrement_with_multiple_stats_and_explicit_value_and_partial_samplerate() {
        subject.decrement( ['test.decr', 'test2.decr'], -100, 0.5 )
        assert resultText == ['test.decr:-100|c|@0.500000', 'test2.decr:-100|c|@0.500000']
    }



}