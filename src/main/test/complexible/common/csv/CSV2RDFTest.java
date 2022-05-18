package complexible.common.csv;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.openrdf.rio.RDFFormat;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.Assert.*;


public class CSV2RDFTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    private CSV2RDF csv2rdf;

    private String TEST_OUTPUT = "src\\main\\testResources\\testOutput.ttl";
    private String EMPTY_INPUT = "src\\main\\testResources\\emptyInput.csv";
    private String TEMPLATE = "src\\main\\testResources\\template.ttl";
    private String CARS_CSV = "src\\main\\testResources\\cars.csv";

    @Before
    public void setUp() throws IOException {
        csv2rdf = new CSV2RDF();
        csv2rdf.files = new ArrayList<>();

        csv2rdf.files.add(0,TEMPLATE);
        csv2rdf.files.add(1, CARS_CSV);

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

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    public IllegalArgumentException expectedEx = new IllegalArgumentException();
    public RuntimeException runtimeException = new RuntimeException();


    @Test
    public void wrongNumberOfArguments() {
        /**Less arguments(2)*/
        expectedEx = Assertions.assertThrows(IllegalArgumentException.class, () -> csv2rdf.run());
        assertEquals("Missing arguments", expectedEx.getMessage());

        /**More arguments(4)*/
        csv2rdf.files.add("a");
        csv2rdf.files.add("b");
        expectedEx = Assertions.assertThrows(IllegalArgumentException.class, () -> csv2rdf.run());
        assertEquals("Too many arguments", expectedEx.getMessage());
    }

    @Test
    public void InicializeFiles(){
        csv2rdf.files.add(2, new File("/testfile.ttl").getPath());
        runtimeException = Assertions.assertThrows(RuntimeException.class, () -> csv2rdf.run());

        assertEquals("CSV to RDF conversion started...\r\n" +
                "Template: " + TEMPLATE + "\r\n" +
                "Input   : "+ CARS_CSV + "\r\n" +
                "Output  : \\testfile.ttl\r\n", outContent.toString());
    }

    @Test
    public void emptyInputFile(){
        csv2rdf.files.set(1, EMPTY_INPUT);
        csv2rdf.files.add(2, new File("/testfile.ttl").getPath());
        runtimeException = Assertions.assertThrows(RuntimeException.class, () -> csv2rdf.run());
        assertTrue(runtimeException.getMessage().endsWith("Input file is empty!"));
    }

    @Test
    public void testRun_noHeader() {
        csv2rdf.files.add(2, new File(TEST_OUTPUT).getPath());
        csv2rdf.run();

        assertSame(RDFFormat.TURTLE, csv2rdf.writer.getRDFFormat());
        assertEquals(true, outContent.toString().contains("Converted 4 rows to 76 triples"));
    }

    @Test
    public void testRun_header(){
        //TODO!!
    }
}