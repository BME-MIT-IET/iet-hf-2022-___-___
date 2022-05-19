package converter;


import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CSV2RDFTest {
    private CSV2RDF csv2rdf;
    ListAppender<ILoggingEvent> listAppender;

    private final String TEST_OUTPUT = "src/test/resources/testOutput.ttl";
    private final String EMPTY_INPUT = "src/test/resources/emptyInput.csv";
    private final String TEMPLATE = "src/test/resources/template.ttl";
    private final String CARS_CSV = "src/test/resources/cars.csv";

    @BeforeEach
    public void setUp() throws IOException {
        csv2rdf = new CSV2RDF();
        csv2rdf.files = new ArrayList<>();

        csv2rdf.files.add(0, TEMPLATE);
        csv2rdf.files.add(1, CARS_CSV);

        Logger fooLogger = (Logger) LoggerFactory.getLogger(CSV2RDF.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        fooLogger.addAppender(listAppender);

        /**testOutput.tll needs to be empty before every test*/
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(TEST_OUTPUT));
        try {
            writer.write("");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public IllegalArgumentException expectedEx = new IllegalArgumentException();
    public NullPointerException nullPointerException = new NullPointerException();


    @Test
    public void lessArguments() {//2
        expectedEx = assertThrows(IllegalArgumentException.class, () -> csv2rdf.run());
        assertEquals("Missing arguments", expectedEx.getMessage());

    }

    @Test
    public void moreArguments() {//4
        /**More arguments(4)*/
        csv2rdf.files.add("a");
        csv2rdf.files.add("b");
        expectedEx = assertThrows(IllegalArgumentException.class, () -> csv2rdf.run());
        assertEquals("Too many arguments", expectedEx.getMessage());
    }

    @Test
    public void testRun_noHeader(){
        csv2rdf.files.add(2, new File(TEST_OUTPUT).getPath());
        csv2rdf.run();

        // JUnit assertions
        List<ILoggingEvent> logsList = listAppender.list;

        assertEquals(Level.INFO, logsList.get(0).getLevel());
        assertEquals(Level.INFO, logsList.get(1).getLevel());

        assertEquals("CSV to RDF conversion started...", logsList.get(0).getMessage());
        assertEquals("Template: " + TEMPLATE, logsList.get(1).getMessage());
        assertEquals("Input   : "+ CARS_CSV, logsList.get(2).getMessage());
        assertEquals("Output  : " + TEST_OUTPUT, logsList.get(3).getMessage());
        assertEquals("Converted 4 rows to 76 triples\r\n", logsList.get(4).getMessage());
    }

    @Test
    public void emptyInputFile() {
        csv2rdf.files.set(1, EMPTY_INPUT);
        csv2rdf.files.add(2, TEST_OUTPUT);

        nullPointerException = assertThrows(NullPointerException.class, () -> csv2rdf.run());
    }

    @Test
    public void wrongOutputFile() {
        csv2rdf.files.add(2, new File("/wrong").getPath()); //DOES NOT EXIST
        csv2rdf.run();
        List<ILoggingEvent> logsList = listAppender.list;

        assertEquals(Level.ERROR, logsList.get(4).getLevel());
        assertTrue(logsList.get(4).getMessage().contains("A hozzáférés megtagadva"));
    }

    @Test
    public void testRun_header() {
        csv2rdf.files.set(1, new File("src/test/resources/cars_noHeader.csv").getPath());
        csv2rdf.files.add(2, new File(TEST_OUTPUT).getPath());
        csv2rdf.noHeader = true;
        expectedEx = assertThrows(IllegalArgumentException.class, () -> csv2rdf.run());
    }

    @Test
    public void templateNotExist(){
        csv2rdf.files.set(0, new File("/wrontTemplate").getPath());
        csv2rdf.files.add(2, new File(TEST_OUTPUT).getPath());
        List<ILoggingEvent> logsList = listAppender.list;
        csv2rdf.run();
        assertEquals(Level.ERROR, logsList.get(4).getLevel());
        assertTrue(logsList.get(4).getMessage().contains("A rendszer nem találja a megadott fájlt"));
    }

    @Test
    public void templateWrong(){
        csv2rdf.files.set(0, new File("src/test/resources/template_wrong.ttl").getPath()); //DOES NOT EXIST
        csv2rdf.files.add(2, new File(TEST_OUTPUT).getPath());
        expectedEx = assertThrows(IllegalArgumentException.class, () -> csv2rdf.run());
    }
}
