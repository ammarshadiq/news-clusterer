/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * IndPOSTagSetView.java
 *
 * Created on Mar 13, 2011, 2:46:05 PM
 */

package newsclusteringlucene.phrasesextractor;

import java.util.ArrayList;

/**
 *
 * @author shadiq
 */
public class POSTagSetView extends javax.swing.JDialog {

    /** Creates new form IndPOSTagSetView */
    public POSTagSetView(java.awt.Frame parent, boolean modal,
            String title, ArrayList<String[]> tagSetDescriptions) {
        super(parent, modal);
        initComponents();
        this.POSTtitleLabel.setText(title+" ("+tagSetDescriptions.size()+" Tags)");
        for (String[] strings : tagSetDescriptions) {
            ((javax.swing.table.DefaultTableModel)POSTDescriptionTable.getModel()).addRow(strings);
        }
    }

    public static void showDialog(java.awt.Frame parent, boolean modal,
            String title, ArrayList<String[]> tagSetDescriptions) {
        posTagSetView = new POSTagSetView (parent, modal, title, tagSetDescriptions);
        posTagSetView.setLocationRelativeTo(parent);
        posTagSetView.setVisible(true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        POSTtitleLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        POSTDescriptionTable = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        okButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(newsclusteringlucene.NewsClusteringLuceneApp.class).getContext().getResourceMap(POSTagSetView.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setModalExclusionType(java.awt.Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);
        setName("Form"); // NOI18N

        POSTtitleLabel.setText(resourceMap.getString("POSTtitleLabel.text")); // NOI18N
        POSTtitleLabel.setName("POSTtitleLabel"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        POSTDescriptionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "POS Tag", "POS Name", "Example"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        POSTDescriptionTable.setName("POSTDescriptionTable"); // NOI18N
        jScrollPane1.setViewportView(POSTDescriptionTable);

        jSeparator1.setName("jSeparator1"); // NOI18N

        okButton.setText(resourceMap.getString("okButton.text")); // NOI18N
        okButton.setName("okButton"); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 361, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(POSTtitleLabel)
                        .addContainerGap(257, Short.MAX_VALUE))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(POSTtitleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(okButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        posTagSetView.setVisible(false);
        dispose();
    }//GEN-LAST:event_okButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable POSTDescriptionTable;
    private javax.swing.JLabel POSTtitleLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables

    private static POSTagSetView posTagSetView;
}
