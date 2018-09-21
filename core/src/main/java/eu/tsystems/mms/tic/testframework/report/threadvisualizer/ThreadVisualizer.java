/*
 * (C) Copyright T-Systems Multimedia Solutions GmbH 2018, ..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Peter Lehmann <p.lehmann@t-systems.com>
 *     pele <p.lehmann@t-systems.com>
 */
/* 
 * Created on 04.01.2013
 * 
 * Copyright(c) 2011 - 2011 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.report.threadvisualizer;

import eu.tsystems.mms.tic.testframework.exceptions.FennecRuntimeException;
import eu.tsystems.mms.tic.testframework.report.model.context.report.Report;
import eu.tsystems.mms.tic.testframework.report.utils.ReportUtils;
import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: peter Date: 16.12.12 Time: 13:44 To change this template use File | Settings | File
 * Templates.
 */
public class ThreadVisualizer {

    /** Logger instance */
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadVisualizer.class);

    /**
     * Check if ThreadVisualizer DataStorage has Data.
     * 
     * @return true if DataStorage is not empty, false otherwise.
     */
    public static boolean hasData() {
        return DataStorage.getList().size() > 0;
    }

    /**
     * generate the html file.
     */
    public static void generateReport() {
        LOGGER.trace("Generate Report with " + DataStorage.getList().size() + " datasets");

        final String tvFolderName = "threadvisualizer";
        final File tvDir = new File(Report.REPORT_DIRECTORY, tvFolderName);

        final String threadVisualizerInputFile = "threads.vm";
        final String threadVisualizerOutputFile = "threads.html";

        final String css = tvFolderName + "/timeline.css";
        final InputStream cssIS = Thread.currentThread().getContextClassLoader().getResourceAsStream(css);
        if (cssIS == null) {
            throw new FennecRuntimeException(css + " not found");
        }

        final String js = tvFolderName + "/timeline.js";
        final InputStream jsIS = Thread.currentThread().getContextClassLoader().getResourceAsStream(js);
        if (jsIS == null) {
            throw new FennecRuntimeException(js + " not found");
        }

        // copy
        ReportUtils.copyFile(css, tvDir);
        ReportUtils.copyFile(js, tvDir);

        /*
         #### Velocity merge
        */

        VelocityContext context = new VelocityContext();

        context.put("reportName", ReportUtils.getReportName());

        // build data
        StringBuilder sb = new StringBuilder();
        String line;
        final List<DataSet> list = DataStorage.getList();
        for (final DataSet dataSet : list) {
            line = "data.addRow(" +
                    "[" +
                    "new Date(" + dataSet.getStartTime() + ")," +
                    "new Date(" + dataSet.getStopTime() + ")," +
                    "'" + dataSet.getContent() + "'," +
                    "'" + dataSet.getThreadName() + "'" +
                    "]" +
                    ");";
            sb.append(line).append("\n");
        }

        context.put("data", sb.toString());

        ReportUtils.addExtraTopLevelTab(threadVisualizerInputFile, threadVisualizerOutputFile, "Threads", "threads", context, false);
    }

}
