package com.PDFTool;

import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * PDF通用操作界面工具(包含：提取、合并、删除)
 *
 * @author ART
 * @since 2024-05-23
 */
public class PdfToolGUI extends JFrame {
    // 选择待提取文件的PDF文件文字框
    private JTextField extractFilesField;
    // 输入提取页码开始的文字框
    private JTextField extractStartPagesField;
    // 输入提取页码结束的文字框
    private JTextField extractEndPagesField;
    // 执行提取按钮
    private JButton extractFilesButton;

    // 选择待合并文件的PDF文件文字框
    private JTextField mergeFilesField;
    // 执行合并按钮
    private JButton mergeFilesButton;

    // 选择待删除文件的PDF文件文字框
    private JTextField deleteFilePathField;
    // 输入删除页码的文字框
    private JTextField deletePagesField;
    // 执行删除按钮
    private JButton deletePagesButton;

    // 结果输出框
    private JTextArea outputTextArea;


    public PdfToolGUI() {
        setTitle("PDF操作小工具");
        setSize(1000, 970);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        // 居中显示
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        getContentPane().add(panel);
        panel.setLayout(null);

        Font font = new Font("宋体", Font.PLAIN, 24);

        /**
         * 1、提取PDF文件界面模块
         */
        JLabel extractFileLabel = new JLabel("提取PDF文件:");
        /*
         x – the new x-coordinate of this component
         y – the new y-coordinate of this component
         width – the new width of this component
         height – the new height of this component
         */
        extractFileLabel.setBounds(10, 20, 150, 40);
        extractFileLabel.setFont(font);
        panel.add(extractFileLabel);

        extractFilesField = new JTextField();
        extractFilesField.setBounds(200, 20, 450, 40);
        extractFilesField.setFont(font);
//        extractFilesField.setEditable(false);
        setPlaceholder(extractFilesField, "请选择待提取的PDF文件");
        panel.add(extractFilesField);

        JButton extractBrowseButton = new JButton("选择文件");
        extractBrowseButton.setBounds(670, 20, 140, 40);
        extractBrowseButton.setFont(font);
        panel.add(extractBrowseButton);
        extractBrowseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFont(font);
                fileChooser.setPreferredSize(new Dimension(1200, 800));
                fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
                fileChooser.setMultiSelectionEnabled(false);

                // 获取文件选择器中的各个组件
                Component[] components = fileChooser.getComponents();
                // 设置组件字体大小
                Font font = new Font("宋体", Font.PLAIN, 24);
                for (Component component : components) {
                    if (component instanceof Container) {
                        setFontRecursively((Container) component, font);
                    }
                }

                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    extractFilesField.setText(selectedFile.getAbsolutePath());
                }
            }
        });

        JLabel extractAppointLabel = new JLabel("提取指定页码:");
        extractAppointLabel.setBounds(10, 80, 200, 40);
        extractAppointLabel.setFont(font);
        panel.add(extractAppointLabel);

        extractStartPagesField = new JTextField();
        extractStartPagesField.setBounds(200, 80, 100, 40);
        extractStartPagesField.setFont(font);
        setPlaceholder(extractStartPagesField, " 开始页");
        panel.add(extractStartPagesField);

        JLabel extractPageToPageLabel = new JLabel("------");
        extractPageToPageLabel.setBounds(340, 80, 40, 40);
        panel.add(extractPageToPageLabel);

        extractEndPagesField = new JTextField();
        extractEndPagesField.setBounds(400, 80, 100, 40);
        extractEndPagesField.setFont(font);
        setPlaceholder(extractEndPagesField, " 结束页");
        panel.add(extractEndPagesField);

        extractFilesButton = new JButton("执行提取");
        extractFilesButton.setBounds(10, 140, 150, 40);
        extractFilesButton.setFont(font);
        panel.add(extractFilesButton);
        extractFilesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputTextArea.setText("");
                try {
                    String inputFilePath = extractFilesField.getText();
                    if(StringUtils.isEmpty(inputFilePath) || inputFilePath.equals("请选择待提取的PDF文件")){
                        JOptionPane.showMessageDialog(null, "请选择待提取文件！");
                        return;
                    }
                    // 获取删除页码输入框内容
                    String startPageText = extractStartPagesField.getText();
                    String endPageText = extractEndPagesField.getText();
                    if(StringUtils.isEmpty(startPageText) || startPageText.equals(" 开始页")){
                        JOptionPane.showMessageDialog(null, "请输入指定开始页码！");
                        return;
                    }
                    if(StringUtils.isEmpty(endPageText) || endPageText.equals(" 结束页")){
                        JOptionPane.showMessageDialog(null, "请输入指定结束页码！");
                        return;
                    }
                    // 去除字符串所有空白字符（包括空格、制表符、换行符等）
                    String startText = startPageText.replaceAll("\\s+", "");
                    String endText = endPageText.replaceAll("\\s+", "");
                    // 转为整数
                    int startPage = Integer.parseInt(startText);
                    int endPage = Integer.parseInt(endText);

                    String outputFilePath = inputFilePath.substring(0, inputFilePath.lastIndexOf(File.separator)) + File.separator + "extracted_" + getFormattedDateTime() + ".pdf";
                    PrintStream standardOut = System.out;
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    PrintStream customOut = new PrintStream(outputStream);
                    System.setOut(customOut);
                    ExtractPagesFromPDF.extractFromPDF(inputFilePath, outputFilePath, startPage, endPage);
                    outputTextArea.setText(outputStream.toString());
                    System.setOut(standardOut);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        /**
         * 2、合并PDF文件界面模块
         */
        JLabel mergeFilesLabel = new JLabel("合并PDF文件:");
        mergeFilesLabel.setBounds(10, 200, 150, 40);
        mergeFilesLabel.setFont(font);
        panel.add(mergeFilesLabel);

        mergeFilesField = new JTextField();
        mergeFilesField.setBounds(200, 200, 450, 40);
        mergeFilesField.setFont(font);
        setPlaceholder(mergeFilesField, "请选择待合并的所有PDF文件");
//        mergeFilesField.setEditable(false);
        panel.add(mergeFilesField);

        JButton mergeFilesBrowseButton = new JButton("选择文件");
        mergeFilesBrowseButton.setBounds(670, 200, 140, 40);
        mergeFilesBrowseButton.setFont(font);
        panel.add(mergeFilesBrowseButton);
        mergeFilesBrowseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFont(font);
                fileChooser.setPreferredSize(new Dimension(1200, 800));
                fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
                fileChooser.setMultiSelectionEnabled(true);

                // 获取文件选择器中的各个组件
                Component[] components = fileChooser.getComponents();
                // 设置组件字体大小
                Font font = new Font("宋体", Font.PLAIN, 24);
                for (Component component : components) {
                    if (component instanceof Container) {
                        setFontRecursively((Container) component, font);
                    }
                }

                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File[] selectedFiles = fileChooser.getSelectedFiles();
                    List<String> fileNames = new ArrayList<>();
                    for (File file : selectedFiles) {
                        fileNames.add(file.getAbsolutePath());
                    }
                    mergeFilesField.setText(String.join(",", fileNames));
                }
            }
        });

        mergeFilesButton = new JButton("执行合并");
        mergeFilesButton.setBounds(10, 260, 150, 40);
        mergeFilesButton.setFont(font);
        panel.add(mergeFilesButton);
        mergeFilesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputTextArea.setText("");
                try {
                    String text = mergeFilesField.getText();
                    if(StringUtils.isEmpty(text) || text.equals("请选择待合并的所有PDF文件")){
                        JOptionPane.showMessageDialog(null, "请选择待合并文件！");
                        return;
                    }
                    String[] filesToMerge = text.split(",");
                    for (int i = 0; i < filesToMerge.length; i++) {
                        filesToMerge[i] = filesToMerge[i].trim();
                    }
                    String outputFilePath = filesToMerge[0].substring(0, filesToMerge[0].lastIndexOf(File.separator)) + File.separator + "merged_" + getFormattedDateTime() + ".pdf";
                    PrintStream standardOut = System.out;
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    PrintStream customOut = new PrintStream(outputStream);
                    System.setOut(customOut);
                    MergePDF.mergePdfFiles(filesToMerge, outputFilePath);
                    outputTextArea.setText(outputStream.toString());
                    System.setOut(standardOut);
//                    JOptionPane.showMessageDialog(null, "Files merged successfully.");
                } catch (Exception ex) {
                    ex.printStackTrace();
//                    JOptionPane.showMessageDialog(null, "Error merging files: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        /**
         * 3、删除PDF文件界面模块
         */
        JLabel deleteLabel = new JLabel("删除PDF文件:");
        deleteLabel.setBounds(10, 320, 150, 40);
        deleteLabel.setFont(font);
        panel.add(deleteLabel);

        deleteFilePathField = new JTextField();
        deleteFilePathField.setBounds(200, 320, 450, 40);
        deleteFilePathField.setFont(font);
//        deleteFilePathField.setEditable(false);
        setPlaceholder(deleteFilePathField, "请选择待删除的PDF文件");
        panel.add(deleteFilePathField);

        JButton deleteBrowseButton = new JButton("选择文件");
        deleteBrowseButton.setBounds(670, 320, 140, 40);
        deleteBrowseButton.setFont(font);
        panel.add(deleteBrowseButton);
        deleteBrowseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFont(font);
                fileChooser.setPreferredSize(new Dimension(1200, 800));
                fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
                fileChooser.setMultiSelectionEnabled(false);

                // 获取文件选择器中的各个组件
                Component[] components = fileChooser.getComponents();
                // 设置组件字体大小
                Font font = new Font("宋体", Font.PLAIN, 24);
                for (Component component : components) {
                    if (component instanceof Container) {
                        setFontRecursively((Container) component, font);
                    }
                }

                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    deleteFilePathField.setText(selectedFile.getAbsolutePath());
                }
            }
        });

        JLabel deletePagesLabel = new JLabel("删除指定页码:");
        deletePagesLabel.setBounds(10, 380, 200, 40);
        deletePagesLabel.setFont(font);
        panel.add(deletePagesLabel);

        deletePagesField = new JTextField();
        deletePagesField.setBounds(200, 380, 450, 40);
        deletePagesField.setFont(font);
        setPlaceholder(deletePagesField, "请输入删除的页码，以英文逗号分隔");
        panel.add(deletePagesField);

        deletePagesButton = new JButton("执行删除");
        deletePagesButton.setBounds(10, 440, 150, 40);
        deletePagesButton.setFont(font);
        panel.add(deletePagesButton);
        deletePagesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputTextArea.setText("");
                try {
                    String inputFilePath = deleteFilePathField.getText();
                    if(StringUtils.isEmpty(inputFilePath) || inputFilePath.equals("请选择待删除的PDF文件")){
                        JOptionPane.showMessageDialog(null, "请选择待删除文件！");
                        return;
                    }
                    // 获取删除页码输入框内容
                    String text = deletePagesField.getText();
                    if(StringUtils.isEmpty(text) || text.equals("请输入删除的页码，以英文逗号分隔")){
                        JOptionPane.showMessageDialog(null, "请输入删除的页码，以英文逗号分隔！");
                        return;
                    }
                    // 去除字符串所有空白字符（包括空格、制表符、换行符等）
                    String strText = text.replaceAll("\\s+", "");
                    // 校验strText是否包含中文逗号
                    if(strText.contains("，")){
//                        System.out.println("输入包含中文逗号，请重新输入");
                        JOptionPane.showMessageDialog(null, "输入包含中文逗号，请重新输入");
                        return;
                    }
                    String[] pagesToDeleteStr =strText.split(",");
                    int[] pagesToDelete = new int[pagesToDeleteStr.length];
                    for (int i = 0; i < pagesToDeleteStr.length; i++) {
                        pagesToDelete[i] = Integer.parseInt(pagesToDeleteStr[i].trim());
                    }
                    String outputFilePath = inputFilePath.substring(0, inputFilePath.lastIndexOf(File.separator)) + File.separator + "removed_" + getFormattedDateTime() + ".pdf";
                    PrintStream standardOut = System.out;
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    PrintStream customOut = new PrintStream(outputStream);
                    System.setOut(customOut);
                    DeletePDF.removePages(inputFilePath, outputFilePath, pagesToDelete);
                    outputTextArea.setText(outputStream.toString());
                    System.setOut(standardOut);
//                    JOptionPane.showMessageDialog(null, "指定页面删除成功");
                } catch (Exception ex) {
                    ex.printStackTrace();
//                    JOptionPane.showMessageDialog(null, "Error deleting pages: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        /**
         * 4、打印结果界面模块
         */
        outputTextArea = new JTextArea();
        outputTextArea.setBounds(10, 500, 960, 400);
        outputTextArea.setFont(font);
        outputTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        scrollPane.setBounds(10, 500, 960, 400);
        panel.add(scrollPane);
    }

    // 设置文本框的提示文字
    private static void setPlaceholder(JTextField textField, String text) {
        textField.setForeground(Color.GRAY); // 设置文字颜色为灰色
        textField.setText(text); // 设置提示文字

        // 添加焦点监听器
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // 获取焦点时清空提示文字
                if (textField.getText().equals(text)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK); // 设置文字颜色为黑色
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                // 失去焦点时如果内容为空，显示提示文字
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY); // 设置文字颜色为灰色
                    textField.setText(text);
                }
            }
        });
    }

    // 递归设置组件字体
    private static void setFontRecursively(Container container, Font font) {
        for (Component component : container.getComponents()) {
            if (component instanceof Container) {
                setFontRecursively((Container) component, font);
            }
            component.setFont(font);
        }
    }

    // 定义一个方法来获取格式化的当前日期时间字符串，适合文件名
    private static String getFormattedDateTime() {
        // 获取当前日期和时间
        LocalDateTime now = LocalDateTime.now();

        // 定义适合文件名的日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

        // 格式化当前日期和时间
        return now.format(formatter);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new PdfToolGUI();
            }
        });
    }
}