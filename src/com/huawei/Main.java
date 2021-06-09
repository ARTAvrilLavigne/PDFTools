package com.huawei;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main extends JFrame {
    // Ŀ¼�����
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

        JLabel splitPDFPath = new JLabel("PDF���Ŀ¼:");
        splitPDFPath.setFont(new Font("����", Font.BOLD, 25));
        JButton scanSplitButton = new JButton("���");
        scanSplitButton.setBounds(680, 20, 80, 30);
        scanSplitButton.setFont(new Font("����", Font.BOLD, 18));
        scanSplitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser() {
                    private static final long serialVersionUID = 1;

                    @Override
                    public JDialog createDialog(Component parent) {
                        JDialog dialog = super.createDialog(parent);
                        // // ����Ŀ¼ѡ����С
                        dialog.setMinimumSize(new Dimension(700, 600));
                        return dialog;
                    }
                };
                // ����Ŀ¼ѡ��������С
                updateFont(chooser, new Font("����", Font.BOLD, 18));
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.showOpenDialog(scanSplitButton);
                File file = chooser.getSelectedFile();
                if (file != null) {
                    splitPDFPathText.setText(file.getAbsoluteFile().toString());
                }
            }
        });

        JLabel splitPdfResultName = new JLabel("�����и��ļ���:");
        splitPdfResultName.setFont(new Font("����", Font.BOLD, 25));
        JTextField splitPdfResultNameText = new JTextField();

        JLabel startPage = new JLabel("�и���ʼҳ��:");
        startPage.setFont(new Font("����", Font.BOLD, 25));
        JTextField startPageText = new JTextField();

        JLabel endPage = new JLabel("�и����ҳ��:");
        endPage.setFont(new Font("����", Font.BOLD, 25));
        JTextField endPageText = new JTextField();

        JButton okSplitButton = new JButton("ȷ�Ϸָ�");
        okSplitButton.setBounds(250, 250, 150, 50);
        okSplitButton.setFont(new Font("����", Font.BOLD, 25));


        splitInput.add(splitPDFPath);
        splitInput.add(splitPDFPathText);
        splitInput.add(splitPdfResultName);
        splitInput.add(splitPdfResultNameText);
        splitInput.add(startPage);
        splitInput.add(startPageText);
        splitInput.add(endPage);
        splitInput.add(endPageText);

        // start================�ϲ�PDF����===========================
        JLabel mergePDFPathLabel = new JLabel("�ϲ����PDF�ļ�Ŀ¼:");
        mergePDFPathLabel.setFont(new Font("����", Font.BOLD, 25));

        JLabel mergePDFResultName = new JLabel("���ɺϲ��ļ���:");
        mergePDFResultName.setFont(new Font("����", Font.BOLD, 25));
        JTextField mergePdfResultNameText = new JTextField();

        JButton scanMergeButton = new JButton("���");
        scanMergeButton.setBounds(680, 355, 80, 30);
        scanMergeButton.setFont(new Font("����", Font.BOLD, 18));
        scanMergeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser() {
                    private static final long serialVersionUID = 1;

                    @Override
                    public JDialog createDialog(Component parent) {
                        JDialog dialog = super.createDialog(parent);
                        // // ����Ŀ¼ѡ����С
                        dialog.setMinimumSize(new Dimension(700, 600));
                        return dialog;
                    }
                };
                // ����Ŀ¼ѡ��������С
                updateFont(chooser, new Font("����", Font.BOLD, 18));
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

        JButton okMergeButton = new JButton("ȷ�Ϻϲ�");
        okMergeButton.setBounds(250, 470, 150, 50);
        okMergeButton.setFont(new Font("����", Font.BOLD, 25));
        // end=========================================


        // �������ı���չʾ��
        JTextArea resultShowArea = new JTextArea();
        resultShowArea.setLineWrap(true);
        resultShowArea.setBounds(50, 540, 650, 200);
        resultShowArea.setEditable(false);


        // ����Ԫ�����
        // 1���ָ�PDF�ļ�����
        frame.add(splitInput);
        frame.add(scanSplitButton);
        frame.add(okSplitButton);
        // 2���ϲ�PDF�ļ�����
        frame.add(mergeInput);
        frame.add(scanMergeButton);
        frame.add(okMergeButton);
        // 3��ִ�������������
        frame.add(resultShowArea);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // ������
        okSplitButton.addActionListener(new ActionListener() {
            boolean checkedpass = true;

            public void actionPerformed(ActionEvent e) {
                checkedpass = true;
                // У�������ʽ
                checkEmpty(splitPDFPathText, "PDF���Ŀ¼");
                checkPDF(splitPDFPathText, "PDF���Ŀ¼");
                checkFileExist(splitPDFPathText, "PDF���Ŀ¼");
                checkEmpty(splitPdfResultNameText, "�����и��ļ���");
                checkNumber(startPageText, "�и���ʼҳ");
                checkNumber(endPageText, "�и����ҳ");

                String pdfPath = splitPDFPathText.getText();
                String splitPdfResultName = splitPdfResultNameText.getText();
                String startPage = startPageText.getText();
                String endPage = endPageText.getText();

                if (checkedpass) {
                    // ҵ�����
                    String splitResultPath = FileUtils.getBasePath(pdfPath) + splitPdfResultName + ".pdf";
                    int startNum = Integer.parseInt(startPage);
                    int endNum = Integer.parseInt(endPage);
                    boolean resultFlag = SplitPDFUtils.splitPDFFile(pdfPath, splitResultPath, startNum, endNum);

                    String trueMessage = "�и�PDF�ļ��ɹ��������ļ���Ϊ��" + splitPdfResultName + ".pdf������·��Ϊ��" + splitResultPath;
                    String failMessage = "�и�PDF�ļ�ʧ��!!���������Ƿ���ȷ";
                    String result = resultFlag ? trueMessage : failMessage;
                    resultShowArea.setText("ִ�н����");
                    resultShowArea.append(result);
                }

            }

            //�����Ƿ�ΪPDF�ļ�
            private void checkPDF(JTextField tf, String msg) {
                if (!checkedpass)
                    return;
                String value = tf.getText();
                if (!value.endsWith(".pdf")) {
                    JOptionPane.showMessageDialog(frame, msg + " ������pdf��ʽ");
                    tf.grabFocus();
                    checkedpass = false;
                }
            }

            //�����Ƿ�Ϊ��
            private void checkFileExist(JTextField tf, String msg) {
                if (!checkedpass)
                    return;
                String value = tf.getText();
                File file = new File(value);
                if (!file.exists()) {
                    JOptionPane.showMessageDialog(frame, msg + " �ļ������ڣ�����");
                    tf.grabFocus();
                    checkedpass = false;
                }
            }

            //�����Ƿ�Ϊ��
            private void checkEmpty(JTextField tf, String msg) {
                if (!checkedpass)
                    return;
                String value = tf.getText();
                if (value.length() == 0) {
                    JOptionPane.showMessageDialog(frame, msg + " ����Ϊ��");
                    tf.grabFocus();
                    checkedpass = false;
                }
            }

            //�������������������
            private void checkNumber(JTextField tf, String msg) {
                if (!checkedpass)
                    return;
                String value = tf.getText();
                try {
                    Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(frame, msg + " ����������");
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
                checkMergeSize(split, mergePDFPathText, "�ϲ����PDF�ļ�Ŀ¼");
                for (String filePath : split) {
                    checkEmpty(filePath, mergePDFPathText, "�ϲ����PDF�ļ�Ŀ¼");
                    checkPDF(filePath, mergePDFPathText, "�ϲ����PDF�ļ�Ŀ¼");
                    checkFileExist(filePath, mergePDFPathText, "�ϲ����PDF�ļ�Ŀ¼");
                    basePath = FileUtils.getBasePath(filePath);
                    list.add(filePath);
                }
                checkMergeResultName(mergePdfResultNameText, "���ɺϲ��ļ���");
                String mergeResultName = mergePdfResultNameText.getText();
                if (!basePath.equals("")) {
                    String resultPath = basePath + mergeResultName + ".pdf";
                    if (flag) {
                        String result = "";
                        try {
                            MergePdfUtils.mergePdfFiles(list, resultPath);
                            result = "�ϲ��ɹ����ϲ����ɵ�PDF�ļ�·��Ϊ��" + resultPath;
                        } catch (Exception ee) {
                            result = "�ϲ�ʧ�ܡ�����ϲ����ļ�·����" + pdfPath;
                        }
                        resultShowArea.setText("ִ�н����");
                        resultShowArea.append(result);
                    }
                }
            }

            // �����Ƿ�ΪPDF�ļ�
            private void checkPDF(String value, JTextField tf, String msg) {
                if (!flag)
                    return;
                if (!value.endsWith(".pdf")) {
                    JOptionPane.showMessageDialog(frame, msg + " ������pdf��ʽ");
                    tf.grabFocus();
                    flag = false;
                }
            }

            // �����Ƿ�Ϊ��
            private void checkFileExist(String value, JTextField tf, String msg) {
                if (!flag)
                    return;
                File file = new File(value);
                if (!file.exists()) {
                    JOptionPane.showMessageDialog(frame, msg + " �ļ������ڣ�����");
                    tf.grabFocus();
                    flag = false;
                }
            }

            // ���ϲ�PDF��������2��
            private void checkMergeSize(String[] value, JTextField tf, String msg) {
                if (value.length < 2) {
                    JOptionPane.showMessageDialog(frame, msg + " �ϲ�PDF�ļ�����2��");
                    tf.grabFocus();
                    flag = false;
                }

            }

            // �����Ƿ�Ϊ��
            private void checkEmpty(String value, JTextField tf, String msg) {
                if (!flag)
                    return;
                if (value.length() == 0) {
                    JOptionPane.showMessageDialog(frame, msg + " ����Ϊ��");
                    tf.grabFocus();
                    flag = false;
                }
            }

            private void checkMergeResultName(JTextField tf, String msg) {
                if (!flag)
                    return;
                String value = tf.getText();
                if (value.length() == 0) {
                    JOptionPane.showMessageDialog(frame, msg + " ����Ϊ��");
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
