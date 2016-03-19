/*
 * The MIT License
 *
 * Copyright (c) 2016 The Broad Institute
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 */
package htsjdk.samtools.util;

import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMFileWriter;
import htsjdk.samtools.SAMFileWriterFactory;
import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.SamReader;
import htsjdk.samtools.SamReaderFactory;
import htsjdk.samtools.ValidationStringency;
import htsjdk.samtools.util.Log;
import htsjdk.samtools.util.ProgressLogger;
import htsjdk.samtools.util.zip.DeflaterFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;


/**
 * This is a test for IntelDeflater.
 */

public class IntelDeflaterTest {

    //private static final Log log = Log.getInstance(IntelDeflaterTest.class);

    @Test
    public void TestIntelDeflatorIsLoaded() throws IOException {

       // log.info("BAM File Written using IntelDeflater");
        System.err.print("In TestIntelDeflatorIsLoaded");

        final File inputFile = new File("testdata/htsjdk/samtools/queryname_sorted.sam");
        final boolean eagerDecode = true;

        final File outputFile = File.createTempFile("IndelDeflator", "bam");
        outputFile.deleteOnExit();

        final int compressionLevel = 1;

        Assert.assertTrue(DeflaterFactory.usingIntelDeflater());


        SamReaderFactory readerFactory = SamReaderFactory.makeDefault().validationStringency(ValidationStringency.SILENT);
        if (eagerDecode) {
            readerFactory = readerFactory.enable(SamReaderFactory.Option.EAGERLY_DECODE);
        }

        final SamReader reader = readerFactory.open(inputFile);
        final SAMFileHeader header = reader.getFileHeader();
        final SAMFileWriter writer = new SAMFileWriterFactory().makeBAMWriter(header, true, outputFile, compressionLevel);
      //  final ProgressLogger pl = new ProgressLogger(log, 1000000);
        for (final SAMRecord record : reader) {
            writer.addAlignment(record);
     //       pl.record(record);
        }
        writer.close();
       // log.info("BAM File Written using IntelDeflater");

      //  log.info("Read the outputted BAM now to ensure correctness");

        try (final SamReader outputReader = readerFactory.open(outputFile)) {
            for (final SAMRecord record : outputReader) {
       //         pl.record(record);
            }
        } catch (Exception e) {
            Assert.fail("Error reading record written with the IntelDeflater library");
        }
    }
}
