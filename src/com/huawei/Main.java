package com.huawei;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main extends JFrame {
    // 目录输入框
    private static JTextField splitPDFPathText = new JTextField();

    private static JTextField mergePDFPathText = new JTextField();

    public static void main(String[] args) {
        int gap = 15;
        JFrame frame = new JFrame("PDFTool");
        frame.setSize(900, 820);
        frame.setLocation(200, 200);
        frame.setLayout(null);

        JPanel splitInput = new JPanel();
        splitInput.setBounds(gap, gap, 650, 220);
        splitInput.setLayout(new GridLayout(4, 2, gap, gap));

        JLabel splitPDFPath = new JLabel("PDF存放目录:");
        splitPDFPath.setFont(new Font("宋体", Font.BOLD, 25));
        JButton scanSplitButton = new JButton("浏览");
        scanSplitButton.setBounds(680, 20, 80, 30);
        scanSplitButton.setFont(new Font("宋体", Font.BOLD, 18));
        scanSplitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser() {
                    private static final long serialVersionUID = 1;

                    @Override
                    public JDialog createDialog(Component parent) {
                        JDialog dialog = super.createDialog(parent);
                        // // 设置目录选择框大小
                        dialog.setMinimumSize(new Dimension(700, 600));
                        return dialog;
                    }
                };
                // 设置目录选择框字体大小
                updateFont(chooser, new Font("宋体", Font.BOLD, 18));
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.showOpenDialog(scanSplitButton);
                File file = chooser.getSelectedFile();
                if (file != null) {
                    splitPDFPathText.setText(file.getAbsoluteFile().toString());
                }
            }
        });

        JLabel splitPdfResultName = new JLabel("生成切割文件名:");
        splitPdfResultName.setFont(new Font("宋体", Font.BOLD, 25));
        JTextField splitPdfResultNameText = new JTextField();

        JLabel startPage = new JLabel("切割起始页码:");
        startPage.setFont(new Font("宋体", Font.BOLD, 25));
        JTextField startPageText = new JTextField();

        JLabel endPage = new JLabel("切割结束页码:");
        endPage.setFont(new Font("宋体", Font.BOLD, 25));
        JTextField endPageText = new JTextField();

        JButton okSplitButton = new JButton("确认分割");
        okSplitButton.setBounds(250, 250, 150, 50);
        okSplitButton.setFont(new Font("宋体", Font.BOLD, 25));


        splitInput.add(splitPDFPath);
        splitInput.add(splitPDFPathText);
        splitInput.add(splitPdfResultName);
        splitInput.add(splitPdfResultNameText);
        splitInput.add(startPage);
        splitInput.add(startPageText);
        splitInput.add(endPage);
        splitInput.add(endPageText);

        // start================合并PDF界面===========================
        JLabel mergePDFPathLabel = new JLabel("合并多个PDF文件目录:");
        mergePDFPathLabel.setFont(new Font("宋体", Font.BOLD, 25));

        JLabel mergePDFResultName = new JLabel("生成合并文件名:");
        mergePDFResultName.setFont(new Font("宋体", Font.BOLD, 25));
        JTextField mergePdfResultNameText = new JTextField();

        JButton scanMergeButton = new JButton("浏览");
        scanMergeButton.setBounds(680, 355, 80, 30);
        scanMergeButton.setFont(new Font("宋体", Font.BOLD, 18));
        scanMergeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser() {
                    private static final long serialVersionUID = 1;

                    @Override
                    public JDialog createDialog(Component parent) {
                        JDialog dialog = super.createDialog(parent);
                        // // 设置目录选择框大小
                        dialog.setMinimumSize(new Dimension(700, 600));
                        return dialog;
                    }
                };
                // 设置目录选择框字体大小
                updateFont(chooser, new Font("宋体", Font.BOLD, 18));
                chooser.setMultiSelectionEnabled(true);
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.showOpenDialog(scanMergeButton);
                File[] selectedFiles = chooser.getSelectedFiles();
                if (selectedFiles != null && selectedFiles.length > 0) {
                    StringBuffer buffer = new StringBuffer();
                    for (File file : selectedFiles) {
                        buffer.append(file.getAbsoluteFile().toString());
                        buffer.append(";");
                    }
                    mergePDFPathText.setText(buffer.toString());
                }
            }
        });

        JPanel mergeInput = new JPanel();
        mergeInput.setBounds(20, 350, 650, 110);
        mergeInput.setLayout(new GridLayout(2, 2, gap, gap));
        mergeInput.add(mergePDFPathLabel);
        mergeInput.add(mergePDFPathText);
        mergeInput.add(mergePDFResultName);
        mergeInput.add(mergePdfResultNameText);

        JButton okMergeButton = new JButton("确认合并");
        okMergeButton.setBounds(250, 470, 150, 50);
        okMergeButton.setFont(new Font("宋体", Font.BOLD, 25));
        // end=========================================


        // 输出结果文本域展示框
        JTextArea resultShowArea = new JTextArea();
        resultShowArea.setLineWrap(true);
        resultShowArea.setBounds(50, 540, 650, 200);
        resultShowArea.setEditable(false);


        // 界面元素添加
        // 1、分割PDF文件界面
        frame.add(splitInput);
        frame.add(scanSplitButton);
        frame.add(okSplitButton);
        // 2、合并PDF文件界面
        frame.add(mergeInput);
        frame.add(scanMergeButton);
        frame.add(okMergeButton);
        // 3、执行输出结果界面框
        frame.add(resultShowArea);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // 鼠标监听
        okSplitButton.addActionListener(new ActionListener() {
            boolean checkedpass = true;

            public void actionPerformed(ActionEvent e) {
                checkedpass = true;
                // 校验输入格式
                checkEmpty(splitPDFPathText, "PDF存放目录");
                checkPDF(splitPDFPathText, "PDF存放目录");
                checkFileExist(splitPDFPathText, "PDF存放目录");
                checkEmpty(splitPdfResultNameText, "生成切割文件名");
                checkNumber(startPageText, "切割起始页");
                checkNumber(endPageText, "切割结束页");

                String pdfPath = splitPDFPathText.getText();
                String splitPdfResultName = splitPdfResultNameText.getText();
                String startPage = startPageText.getText();
                String endPage = endPageText.getText();

                if (checkedpass) {
                    // 业务操作
                    String splitResultPath = FileUtils.getBasePath(pdfPath) + splitPdfResultName + ".pdf";
                    int startNum = Integer.parseInt(startPage);
                    int endNum = Integer.parseInt(endPage);
                    boolean resultFlag = SplitPDFUtils.splitPDFFile(pdfPath, splitResultPath, startNum, endNum);

                    String trueMessage = "切割PDF文件成功，生成文件名为：" + splitPdfResultName + ".pdf。完整路径为：" + splitResultPath;
                    String failMessage = "切割PDF文件失败!!请检查输入是否正确";
                    String result = resultFlag ? trueMessage : failMessage;
                    resultShowArea.setText("执行结果：");
                    resultShowArea.append(result);
                }

            }

            //检验是否为PDF文件
            private void checkPDF(JTextField tf, String msg) {
                if (!checkedpass)
                    return;
                String value = tf.getText();
                if (!value.endsWith(".pdf")) {
                    JOptionPane.showMessageDialog(frame, msg + " 必须是pdf格式");
                    tf.grabFocus();
                    checkedpass = false;
                }
            }

            //检验是否为空
            private void checkFileExist(JTextField tf, String msg) {
                if (!checkedpass)
                    return;
                String value = tf.getText();
                File file = new File(value);
                if (!file.exists()) {
                    JOptionPane.showMessageDialog(frame, msg + " 文件不存在，请检查");
                    tf.grabFocus();
                    checkedpass = false;
                }
            }

            //检验是否为空
            private void checkEmpty(JTextField tf, String msg) {
                if (!checkedpass)
                    return;
                String value = tf.getText();
                if (value.length() == 0) {
                    JOptionPane.showMessageDialog(frame, msg + " 不能为空");
                    tf.grabFocus();
                    checkedpass = false;
                }
            }

            //检验输入金额必须是整数
            private void checkNumber(JTextField tf, String msg) {
                if (!checkedpass)
                    return;
                String value = tf.getText();
                try {
                    Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(frame, msg + " 必须是整数");
                    tf.grabFocus();
                    checkedpass = false;
                }
            }
        });

        okMergeButton.addActionListener(new ActionListener() {
            boolean flag = true;

            @Override
            public void actionPerformed(ActionEvent e) {
                flag = true;

                String pdfPath = mergePDFPathText.getText();
                String[] split = pdfPath.split(";");
                String basePath = "";
                List<String> list = new ArrayList<>();
                checkMergeSize(split, mergePDFPathText, "合并多个PDF文件目录");
                for (String filePath : split) {
                    checkEmpty(filePath, mergePDFPathText, "合并多个PDF文件目录");
                    checkPDF(filePath, mergePDFPathText, "合并多个PDF文件目录");
                    checkFileExist(filePath, mergePDFPathText, "合并多个PDF文件目录");
                    basePath = FileUtils.getBasePath(filePath);
                    list.add(filePath);
                }
                checkMergeResultName(mergePdfResultNameText, "生成合并文件名");
                String mergeResultName = mergePdfResultNameText.getText();
                if (!basePath.equals("")) {
                    String resultPath = basePath + mergeResultName + ".pdf";
                    if (flag) {
                        String result = "";
                        try {
                            MergePdfUtils.mergePdfFiles(list, resultPath);
                            result = "合并成功。合并生成的PDF文件路径为：" + resultPath;
                        } catch (Exception ee) {
                            result = "合并失败。请检查合并的文件路径：" + pdfPath;
                        }
                        resultShowArea.setText("执行结果：");
                        resultShowArea.append(result);
                    }
                }
            }

            // 检验是否为PDF文件
            private void checkPDF(String value, JTextField tf, String msg) {
                if (!flag)
                    return;
                if (!value.endsWith(".pdf")) {
                    JOptionPane.showMessageDialog(frame, msg + " 必须是pdf格式");
                    tf.grabFocus();
                    flag = false;
                }
            }

            // 检验是否为空
            private void checkFileExist(String value, JTextField tf, String msg) {
                if (!flag)
                    return;
                File file = new File(value);
                if (!file.exists()) {
                    JOptionPane.showMessageDialog(frame, msg + " 文件不存在，请检查");
                    tf.grabFocus();
                    flag = false;
                }
            }

            // 检查合并PDF数量至少2个
            private void checkMergeSize(String[] value, JTextField tf, String msg) {
                if (value.length < 2) {
                    JOptionPane.showMessageDialog(frame, msg + " 合并PDF文件至少2个");
                    tf.grabFocus();
                    flag = false;
                }

            }

            // 检验是否为空
            private void checkEmpty(String value, JTextField tf, String msg) {
                if (!flag)
                    return;
                if (value.length() == 0) {
                    JOptionPane.showMessageDialog(frame, msg + " 不能为空");
                    tf.grabFocus();
                    flag = false;
                }
            }

            private void checkMergeResultName(JTextField tf, String msg) {
                if (!flag)
                    return;
                String value = tf.getText();
                if (value.length() == 0) {
                    JOptionPane.showMessageDialog(frame, msg + " 不能为空");
                    tf.grabFocus();
                    flag = false;
                }
            }
        });
    }


    private static void updateFont(Component comp, Font font) {
        comp.setFont(font);
        if (comp instanceof Container) {
            Container c = (Container) comp;
            int n = c.getComponentCount();
            for (int i = 0; i < n; i++) {
                updateFont(c.getComponent(i), font);
            }
        }
    }


}
