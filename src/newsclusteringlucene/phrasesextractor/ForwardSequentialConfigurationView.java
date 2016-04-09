/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PhraseExtractorConfigurationView.java
 *
 * Created on Mar 23, 2011, 6:05:46 PM
 */

package newsclusteringlucene.phrasesextractor;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import newsclusteringlucene.NewsClusteringLuceneApp;
import newsclusteringlucene.phrasesextractor.POSTagSetView;
import newsclusteringlucene.phrasesextractor.Pattern;
import newsclusteringlucene.phrasesextractor.PatternElement;
import newsclusteringlucene.phrasesextractor.IndPOSTagger;
import newsclusteringlucene.phrasesextractor.IndPOSTaggerConfigViewDialog;

/**
 *
 * @author shadiq
 */
public class ForwardSequentialConfigurationView extends javax.swing.JDialog {

    /** Creates new form PhraseExtractorConfigurationView */
    public ForwardSequentialConfigurationView(java.awt.Frame parent, boolean modal, ForwardSequentialConfiguration appConf) {
        super(parent, modal);
        initComponents();
        chooser = new JFileChooser();
        setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);

        ForwardSequentialConfigurationView.extractorConfBackup = appConf;
        ForwardSequentialConfigurationView.extractorConfEdit = appConf.clone();

        // Phrase Pattern Filters
        // if its not using Part-of-Speech tag in its pattern, why bother to process its POSTag.
        if (ForwardSequentialConfigurationView.extractorConfEdit.usesPOSTagger) {
            usesPOSTCheckBox.setSelected(true);
            posTaggerComboBox.setEnabled(true);
            posTaggerAdvanceButton.setEnabled(true);
        } else {
            usesPOSTCheckBox.setSelected(false);
            posTaggerComboBox.setEnabled(false);
            posTaggerAdvanceButton.setEnabled(false);
        }
        if(ForwardSequentialConfigurationView.extractorConfEdit.posTagger != null){
            System.out.println(ForwardSequentialConfigurationView.extractorConfEdit.posTagger.getClass().getName());
            if (ForwardSequentialConfigurationView.extractorConfEdit.posTagger.getClass().getName().equalsIgnoreCase("newsclusteringlucene.phraseextractor.postagger.IndPOSTagger")) {
                posTaggerComboBox.setSelectedIndex(0);
            }
        }

        for (Pattern pattern : ForwardSequentialConfigurationView.extractorConfEdit.patternFilters) {
            patternsListModel.addElement(pattern);
        }
        // backup the data before edit, so when the user hit Cancel button, this backup data is returned
//        patternFiltersBackup = new ArrayList<Pattern>();
//        for (Pattern pattern : PatternFilters) {
//            patternFiltersBackup.add(pattern.clone());
//        }
//        this.usesPOST = usesPOST;
//        this.posTagger = posTagger; ∞

        if (ForwardSequentialConfigurationView.extractorConfEdit.usesPOSTagger) {
            posTagComboBoxModel = new DefaultComboBoxModel(ForwardSequentialConfigurationView.extractorConfEdit.posTagger.getTagSetList());
            posTagComboBox.setModel(posTagComboBoxModel);
            this.seePOSTDescButton.setEnabled(true);
        } else {
            posTagComboBoxModel.removeAllElements();
            this.seePOSTDescButton.setEnabled(false);
        }

        // Include All Possibilities
        if(ForwardSequentialConfigurationView.extractorConfEdit.includeAllPossibilities){
            includeAllPossibilitiesCheckBox.setSelected(true);
        } else {
            includeAllPossibilitiesCheckBox.setSelected(false);
        }

        // Stop Words Filter
        stopWordFileTextField.setText(ForwardSequentialConfigurationView.extractorConfEdit.stopWordsFile.getAbsolutePath());
        if(ForwardSequentialConfigurationView.extractorConfEdit.excludePhraseContainsStopWord){
            this.excludeContainStopWordsCheckBox.setSelected(true);
            this.excludeStartStopWordsCheckBox.setSelected(false);
            this.excludeStartStopWordsCheckBox.setEnabled(false);
            this.excludeEndStopWordsCheckBox.setSelected(false);
            this.excludeEndStopWordsCheckBox.setEnabled(false);
        } else {
            this.excludeContainStopWordsCheckBox.setSelected(false);
            this.excludeStartStopWordsCheckBox.setEnabled(true);
            this.excludeEndStopWordsCheckBox.setEnabled(true);
            if(ForwardSequentialConfigurationView.extractorConfEdit.excludePhraseStartedWithStopWord){
                this.excludeStartStopWordsCheckBox.setSelected(true);
            } else {
                this.excludeStartStopWordsCheckBox.setSelected(false);
            }
            if (ForwardSequentialConfigurationView.extractorConfEdit.excludePhraseEndedWithStopWord){
                this.excludeEndStopWordsCheckBox.setSelected(true);
            } else {
                this.excludeEndStopWordsCheckBox.setSelected(false);
            }
        }

        // Num Of Words Filter
        numOfTermFilterMaxSpinner.getModel().setValue(ForwardSequentialConfigurationView.extractorConfEdit.phraseExtractorMaxN);
        numOfTermFilterMinSpinner.getModel().setValue(ForwardSequentialConfigurationView.extractorConfEdit.phraseExtractorMinN);

        // Megre Overlap Phrases
        if (ForwardSequentialConfigurationView.extractorConfEdit.mergeOverlapPhrases){
            mergeOverlapPhrasesCheckBox.setSelected(true);
            removeOverlappedPhrasesCheckBox.setEnabled(true);
        } else {
            mergeOverlapPhrasesCheckBox.setSelected(false);
            removeOverlappedPhrasesCheckBox.setSelected(false);
            removeOverlappedPhrasesCheckBox.setEnabled(false);
        }
    }

    public static ForwardSequentialConfiguration showDialog(java.awt.Frame parent, boolean modal,
            ForwardSequentialConfiguration appConf){
        phraseExtractorConfigurationView = new ForwardSequentialConfigurationView(parent, modal, appConf);
        phraseExtractorConfigurationView.setLocationRelativeTo(parent);
        phraseExtractorConfigurationView.setVisible(true);
        return extractorConfEdit;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    patternTypebuttonGroup = new javax.swing.ButtonGroup();
    filtersTabbedPane = new javax.swing.JTabbedPane();
    patternFiltersPanel = new javax.swing.JPanel();
    usesPOSTCheckBox = new javax.swing.JCheckBox();
    posTaggerComboBox = new javax.swing.JComboBox();
    posTaggerAdvanceButton = new javax.swing.JButton();
    patternsLabel = new javax.swing.JLabel();
    patternsListScrollPane = new javax.swing.JScrollPane();
    patternsList = new javax.swing.JList();
    patternsAddButton = new javax.swing.JButton();
    patternsRemoveButton = new javax.swing.JButton();
    patternTypeExactRadioButton = new javax.swing.JRadioButton();
    patternTypeFreeRadioButton = new javax.swing.JRadioButton();
    elementsLabel = new javax.swing.JLabel();
    elementsListScrollPane = new javax.swing.JScrollPane();
    elementsList = new javax.swing.JList();
    elementsAddButton = new javax.swing.JButton();
    elementsRemoveButton = new javax.swing.JButton();
    elementLabel = new javax.swing.JLabel();
    elementTypeComboBox = new javax.swing.JComboBox();
    posTagComboBox = new javax.swing.JComboBox();
    elementStringTextField = new javax.swing.JTextField();
    seePOSTDescButton = new javax.swing.JButton();
    includeAllPossibilitiesCheckBox = new javax.swing.JCheckBox();
    miscFiltersPanel = new javax.swing.JPanel();
    stopWordFiltersLabel = new javax.swing.JLabel();
    stopWordFiltersSeparator = new javax.swing.JSeparator();
    stopWordFileLabel = new javax.swing.JLabel();
    stopWordFileTextField = new javax.swing.JTextField();
    stopWordFileBrowseButton = new javax.swing.JButton();
    SWFilterExcludeLabel = new javax.swing.JLabel();
    excludeContainStopWordsCheckBox = new javax.swing.JCheckBox();
    excludeStartStopWordsCheckBox = new javax.swing.JCheckBox();
    excludeEndStopWordsCheckBox = new javax.swing.JCheckBox();
    numOfTermFiltersLabel = new javax.swing.JLabel();
    numOfTermFiltersSeparator = new javax.swing.JSeparator();
    numOfTermFilterMinLabel = new javax.swing.JLabel();
    numOfTermFilterMinSpinner = new javax.swing.JSpinner();
    numOfTermFilterMaxLabel = new javax.swing.JLabel();
    numOfTermFilterMaxSpinner = new javax.swing.JSpinner();
    overlapPhrasesLabel = new javax.swing.JLabel();
    overlapPhrasesSeparator = new javax.swing.JSeparator();
    mergeOverlapPhrasesCheckBox = new javax.swing.JCheckBox();
    removeOverlappedPhrasesCheckBox = new javax.swing.JCheckBox();
    okButton = new javax.swing.JButton();
    cancelButton = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(newsclusteringlucene.NewsClusteringLuceneApp.class).getContext().getResourceMap(ForwardSequentialConfigurationView.class);
    setTitle(resourceMap.getString("Form.title")); // NOI18N
    setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);
    setName("Form"); // NOI18N

    filtersTabbedPane.setName("filtersTabbedPane"); // NOI18N

    patternFiltersPanel.setName("patternFiltersPanel"); // NOI18N

    usesPOSTCheckBox.setSelected(true);
    usesPOSTCheckBox.setText(resourceMap.getString("usesPOSTCheckBox.text")); // NOI18N
    usesPOSTCheckBox.setName("usesPOSTCheckBox"); // NOI18N
    usesPOSTCheckBox.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        usesPOSTCheckBoxItemStateChanged(evt);
      }
    });

    posTaggerComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "iPOSTagger 1.0" }));
    posTaggerComboBox.setName("posTaggerComboBox"); // NOI18N

    posTaggerAdvanceButton.setText(resourceMap.getString("posTaggerAdvanceButton.text")); // NOI18N
    posTaggerAdvanceButton.setName("posTaggerAdvanceButton"); // NOI18N
    posTaggerAdvanceButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        posTaggerAdvanceButtonActionPerformed(evt);
      }
    });

    patternsLabel.setText(resourceMap.getString("patternsLabel.text")); // NOI18N
    patternsLabel.setName("patternsLabel"); // NOI18N

    patternsListScrollPane.setName("patternsListScrollPane"); // NOI18N

    patternsList.setModel(new javax.swing.AbstractListModel() {
      String[] strings = { "/NN /NNG /NNP (Free)", "/NN /VBT /NN (Exact)", "/NNP /VBT /NN (Exact)", "etc.." };
      public int getSize() { return strings.length; }
      public Object getElementAt(int i) { return strings[i]; }
    });
    patternsList.setModel(this.patternsListModel);
    patternsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    patternsList.setName("patternsList"); // NOI18N
    patternsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
      public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
        patternsListValueChanged(evt);
      }
    });
    patternsListScrollPane.setViewportView(patternsList);

    patternsAddButton.setText(resourceMap.getString("patternsAddButton.text")); // NOI18N
    patternsAddButton.setName("patternsAddButton"); // NOI18N
    patternsAddButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        patternsAddButtonActionPerformed(evt);
      }
    });

    patternsRemoveButton.setText(resourceMap.getString("patternsRemoveButton.text")); // NOI18N
    patternsRemoveButton.setName("patternsRemoveButton"); // NOI18N
    patternsRemoveButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        patternsRemoveButtonActionPerformed(evt);
      }
    });

    patternTypebuttonGroup.add(patternTypeExactRadioButton);
    patternTypeExactRadioButton.setText(resourceMap.getString("patternTypeExactRadioButton.text")); // NOI18N
    patternTypeExactRadioButton.setEnabled(false);
    patternTypeExactRadioButton.setName("patternTypeExactRadioButton"); // NOI18N
    patternTypeExactRadioButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        patternTypeExactRadioButtonActionPerformed(evt);
      }
    });

    patternTypebuttonGroup.add(patternTypeFreeRadioButton);
    patternTypeFreeRadioButton.setText(resourceMap.getString("patternTypeFreeRadioButton.text")); // NOI18N
    patternTypeFreeRadioButton.setEnabled(false);
    patternTypeFreeRadioButton.setName("patternTypeFreeRadioButton"); // NOI18N
    patternTypeFreeRadioButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        patternTypeFreeRadioButtonActionPerformed(evt);
      }
    });

    elementsLabel.setText(resourceMap.getString("elementsLabel.text")); // NOI18N
    elementsLabel.setName("elementsLabel"); // NOI18N

    elementsListScrollPane.setName("elementsListScrollPane"); // NOI18N

    elementsList.setModel(new javax.swing.AbstractListModel() {
      String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
      public int getSize() { return strings.length; }
      public Object getElementAt(int i) { return strings[i]; }
    });
    elementsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    elementsList.setModel(this.elementsListModel);
    elementsList.setName("elementsList"); // NOI18N
    elementsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
      public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
        elementsListValueChanged(evt);
      }
    });
    elementsListScrollPane.setViewportView(elementsList);

    elementsAddButton.setText(resourceMap.getString("elementsAddButton.text")); // NOI18N
    elementsAddButton.setName("elementsAddButton"); // NOI18N
    elementsAddButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        elementsAddButtonActionPerformed(evt);
      }
    });

    elementsRemoveButton.setText(resourceMap.getString("elementsRemoveButton.text")); // NOI18N
    elementsRemoveButton.setName("elementsRemoveButton"); // NOI18N
    elementsRemoveButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        elementsRemoveButtonActionPerformed(evt);
      }
    });

    elementLabel.setText(resourceMap.getString("elementLabel.text")); // NOI18N
    elementLabel.setEnabled(false);
    elementLabel.setName("elementLabel"); // NOI18N

    elementTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Word", "POS Tag" }));
    elementTypeComboBox.setName("elementTypeComboBox"); // NOI18N
    elementTypeComboBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        elementTypeComboBoxActionPerformed(evt);
      }
    });
    elementTypeComboBox.setEnabled(false); // first instantiation, hide it

    posTagComboBox.setModel(this.posTagComboBoxModel);
    posTagComboBox.setName("posTagComboBox"); // NOI18N
    posTagComboBox.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        posTagComboBoxItemStateChanged(evt);
      }
    });
    posTagComboBox.setEnabled(false); // first instantiation, hide it

    elementStringTextField.setName("elementStringTextField"); // NOI18N
    elementStringTextField.setEnabled(false); // first instantiation, hide it
    elementStringTextField.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(java.awt.event.KeyEvent evt) {
        elementStringTextFieldKeyTyped(evt);
      }
    });

    seePOSTDescButton.setText(resourceMap.getString("seePOSTDescButton.text")); // NOI18N
    seePOSTDescButton.setName("seePOSTDescButton"); // NOI18N
    seePOSTDescButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        seePOSTDescButtonActionPerformed(evt);
      }
    });

    includeAllPossibilitiesCheckBox.setText(resourceMap.getString("includeAllPossibilitiesCheckBox.text")); // NOI18N
    includeAllPossibilitiesCheckBox.setName("includeAllPossibilitiesCheckBox"); // NOI18N

    javax.swing.GroupLayout patternFiltersPanelLayout = new javax.swing.GroupLayout(patternFiltersPanel);
    patternFiltersPanel.setLayout(patternFiltersPanelLayout);
    patternFiltersPanelLayout.setHorizontalGroup(
      patternFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(patternFiltersPanelLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(patternFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(patternFiltersPanelLayout.createSequentialGroup()
            .addGroup(patternFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(patternsLabel)
              .addGroup(patternFiltersPanelLayout.createSequentialGroup()
                .addGroup(patternFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                  .addGroup(javax.swing.GroupLayout.Alignment.LEADING, patternFiltersPanelLayout.createSequentialGroup()
                    .addComponent(elementLabel)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(elementTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(posTagComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(elementStringTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE))
                  .addComponent(elementsLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(elementsListScrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                  .addGroup(patternFiltersPanelLayout.createSequentialGroup()
                    .addComponent(patternTypeExactRadioButton)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(patternTypeFreeRadioButton))
                  .addComponent(patternsListScrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                  .addGroup(javax.swing.GroupLayout.Alignment.LEADING, patternFiltersPanelLayout.createSequentialGroup()
                    .addComponent(usesPOSTCheckBox)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(posTaggerComboBox, 0, 166, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(posTaggerAdvanceButton)))
                .addGap(6, 6, 6)
                .addGroup(patternFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                  .addComponent(patternsAddButton)
                  .addComponent(patternsRemoveButton)
                  .addComponent(elementsAddButton)
                  .addComponent(elementsRemoveButton))))
            .addGap(15, 15, 15))
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, patternFiltersPanelLayout.createSequentialGroup()
            .addComponent(seePOSTDescButton)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 118, Short.MAX_VALUE)
            .addComponent(includeAllPossibilitiesCheckBox)
            .addContainerGap())))
    );

    patternFiltersPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {elementsAddButton, elementsRemoveButton, patternsAddButton, patternsRemoveButton, posTaggerAdvanceButton});

    patternFiltersPanelLayout.setVerticalGroup(
      patternFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(patternFiltersPanelLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(patternFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(usesPOSTCheckBox)
          .addComponent(posTaggerComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(posTaggerAdvanceButton))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(patternsLabel)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(patternFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(patternFiltersPanelLayout.createSequentialGroup()
            .addComponent(patternsListScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(patternFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(patternTypeExactRadioButton)
              .addComponent(patternTypeFreeRadioButton))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(elementsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(elementsListScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(patternFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(elementLabel)
              .addComponent(elementTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(posTagComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(elementStringTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
          .addGroup(patternFiltersPanelLayout.createSequentialGroup()
            .addComponent(patternsAddButton)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(patternsRemoveButton)
            .addGap(58, 58, 58)
            .addComponent(elementsAddButton)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(elementsRemoveButton)))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(patternFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(includeAllPossibilitiesCheckBox)
          .addComponent(seePOSTDescButton))
        .addContainerGap(20, Short.MAX_VALUE))
    );

    filtersTabbedPane.addTab(resourceMap.getString("patternFiltersPanel.TabConstraints.tabTitle"), patternFiltersPanel); // NOI18N

    miscFiltersPanel.setName("miscFiltersPanel"); // NOI18N

    stopWordFiltersLabel.setFont(resourceMap.getFont("stopWordFiltersLabel.font")); // NOI18N
    stopWordFiltersLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    stopWordFiltersLabel.setText(resourceMap.getString("stopWordFiltersLabel.text")); // NOI18N
    stopWordFiltersLabel.setName("stopWordFiltersLabel"); // NOI18N

    stopWordFiltersSeparator.setName("stopWordFiltersSeparator"); // NOI18N

    stopWordFileLabel.setFont(resourceMap.getFont("stopWordFileLabel.font")); // NOI18N
    stopWordFileLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    stopWordFileLabel.setText(resourceMap.getString("stopWordFileLabel.text")); // NOI18N
    stopWordFileLabel.setName("stopWordFileLabel"); // NOI18N

    stopWordFileTextField.setName("stopWordFileTextField"); // NOI18N

    stopWordFileBrowseButton.setText(resourceMap.getString("stopWordFileBrowseButton.text")); // NOI18N
    stopWordFileBrowseButton.setName("stopWordFileBrowseButton"); // NOI18N
    stopWordFileBrowseButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        stopWordFileBrowseButtonActionPerformed(evt);
      }
    });

    SWFilterExcludeLabel.setFont(resourceMap.getFont("SWFilterExcludeLabel.font")); // NOI18N
    SWFilterExcludeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    SWFilterExcludeLabel.setText(resourceMap.getString("SWFilterExcludeLabel.text")); // NOI18N
    SWFilterExcludeLabel.setName("SWFilterExcludeLabel"); // NOI18N

    excludeContainStopWordsCheckBox.setText(resourceMap.getString("excludeContainStopWordsCheckBox.text")); // NOI18N
    excludeContainStopWordsCheckBox.setName("excludeContainStopWordsCheckBox"); // NOI18N
    excludeContainStopWordsCheckBox.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        excludeContainStopWordsCheckBoxItemStateChanged(evt);
      }
    });

    excludeStartStopWordsCheckBox.setSelected(true);
    excludeStartStopWordsCheckBox.setText(resourceMap.getString("excludeStartStopWordsCheckBox.text")); // NOI18N
    excludeStartStopWordsCheckBox.setName("excludeStartStopWordsCheckBox"); // NOI18N

    excludeEndStopWordsCheckBox.setSelected(true);
    excludeEndStopWordsCheckBox.setText(resourceMap.getString("excludeEndStopWordsCheckBox.text")); // NOI18N
    excludeEndStopWordsCheckBox.setName("excludeEndStopWordsCheckBox"); // NOI18N

    numOfTermFiltersLabel.setFont(resourceMap.getFont("numOfTermFiltersLabel.font")); // NOI18N
    numOfTermFiltersLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    numOfTermFiltersLabel.setText(resourceMap.getString("numOfTermFiltersLabel.text")); // NOI18N
    numOfTermFiltersLabel.setName("numOfTermFiltersLabel"); // NOI18N

    numOfTermFiltersSeparator.setName("numOfTermFiltersSeparator"); // NOI18N

    numOfTermFilterMinLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    numOfTermFilterMinLabel.setText(resourceMap.getString("numOfTermFilterMinLabel.text")); // NOI18N
    numOfTermFilterMinLabel.setName("numOfTermFilterMinLabel"); // NOI18N

    numOfTermFilterMinSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
    numOfTermFilterMinSpinner.setName("numOfTermFilterMinSpinner"); // NOI18N
    numOfTermFilterMinSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        numOfTermFilterMinSpinnerStateChanged(evt);
      }
    });

    numOfTermFilterMaxLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    numOfTermFilterMaxLabel.setText(resourceMap.getString("numOfTermFilterMaxLabel.text")); // NOI18N
    numOfTermFilterMaxLabel.setName("numOfTermFilterMaxLabel"); // NOI18N

    numOfTermFilterMaxSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
    numOfTermFilterMaxSpinner.setName("numOfTermFilterMaxSpinner"); // NOI18N
    numOfTermFilterMaxSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        numOfTermFilterMaxSpinnerStateChanged(evt);
      }
    });

    overlapPhrasesLabel.setFont(resourceMap.getFont("overlapPhrasesLabel.font")); // NOI18N
    overlapPhrasesLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    overlapPhrasesLabel.setText(resourceMap.getString("overlapPhrasesLabel.text")); // NOI18N
    overlapPhrasesLabel.setName("overlapPhrasesLabel"); // NOI18N

    overlapPhrasesSeparator.setName("overlapPhrasesSeparator"); // NOI18N

    mergeOverlapPhrasesCheckBox.setText(resourceMap.getString("mergeOverlapPhrasesCheckBox.text")); // NOI18N
    mergeOverlapPhrasesCheckBox.setName("mergeOverlapPhrasesCheckBox"); // NOI18N
    mergeOverlapPhrasesCheckBox.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        mergeOverlapPhrasesCheckBoxItemStateChanged(evt);
      }
    });

    removeOverlappedPhrasesCheckBox.setText(resourceMap.getString("removeOverlappedPhrasesCheckBox.text")); // NOI18N
    removeOverlappedPhrasesCheckBox.setEnabled(false);
    removeOverlappedPhrasesCheckBox.setName("removeOverlappedPhrasesCheckBox"); // NOI18N
    removeOverlappedPhrasesCheckBox.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        removeOverlappedPhrasesCheckBoxItemStateChanged(evt);
      }
    });

    javax.swing.GroupLayout miscFiltersPanelLayout = new javax.swing.GroupLayout(miscFiltersPanel);
    miscFiltersPanel.setLayout(miscFiltersPanelLayout);
    miscFiltersPanelLayout.setHorizontalGroup(
      miscFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(miscFiltersPanelLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(miscFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(miscFiltersPanelLayout.createSequentialGroup()
            .addGap(12, 12, 12)
            .addGroup(miscFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(miscFiltersPanelLayout.createSequentialGroup()
                .addComponent(SWFilterExcludeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(miscFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(excludeEndStopWordsCheckBox)
                  .addComponent(excludeStartStopWordsCheckBox)
                  .addComponent(excludeContainStopWordsCheckBox)))
              .addGroup(miscFiltersPanelLayout.createSequentialGroup()
                .addComponent(stopWordFileLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stopWordFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stopWordFileBrowseButton))))
          .addGroup(miscFiltersPanelLayout.createSequentialGroup()
            .addComponent(stopWordFiltersLabel)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(stopWordFiltersSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE))
          .addGroup(miscFiltersPanelLayout.createSequentialGroup()
            .addGap(12, 12, 12)
            .addComponent(mergeOverlapPhrasesCheckBox)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(removeOverlappedPhrasesCheckBox))
          .addGroup(miscFiltersPanelLayout.createSequentialGroup()
            .addComponent(overlapPhrasesLabel)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(overlapPhrasesSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE))
          .addGroup(miscFiltersPanelLayout.createSequentialGroup()
            .addGap(12, 12, 12)
            .addComponent(numOfTermFilterMinLabel)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(numOfTermFilterMinSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(numOfTermFilterMaxLabel)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(numOfTermFilterMaxSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(miscFiltersPanelLayout.createSequentialGroup()
            .addComponent(numOfTermFiltersLabel)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(numOfTermFiltersSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)))
        .addContainerGap())
    );
    miscFiltersPanelLayout.setVerticalGroup(
      miscFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(miscFiltersPanelLayout.createSequentialGroup()
        .addGroup(miscFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(miscFiltersPanelLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(stopWordFiltersLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(miscFiltersPanelLayout.createSequentialGroup()
            .addGap(18, 18, 18)
            .addComponent(stopWordFiltersSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(miscFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(stopWordFileLabel)
          .addComponent(stopWordFileTextField)
          .addComponent(stopWordFileBrowseButton))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(miscFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(SWFilterExcludeLabel)
          .addGroup(miscFiltersPanelLayout.createSequentialGroup()
            .addComponent(excludeContainStopWordsCheckBox)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(excludeStartStopWordsCheckBox)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(excludeEndStopWordsCheckBox)))
        .addGap(6, 6, 6)
        .addGroup(miscFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(numOfTermFiltersLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(numOfTermFiltersSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(miscFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(numOfTermFilterMinLabel)
          .addComponent(numOfTermFilterMinSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(numOfTermFilterMaxLabel)
          .addComponent(numOfTermFilterMaxSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGroup(miscFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(miscFiltersPanelLayout.createSequentialGroup()
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(overlapPhrasesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(miscFiltersPanelLayout.createSequentialGroup()
            .addGap(12, 12, 12)
            .addComponent(overlapPhrasesSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(miscFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(mergeOverlapPhrasesCheckBox)
          .addComponent(removeOverlappedPhrasesCheckBox))
        .addGap(78, 78, 78))
    );

    filtersTabbedPane.addTab(resourceMap.getString("miscFiltersPanel.TabConstraints.tabTitle"), miscFiltersPanel); // NOI18N

    okButton.setText(resourceMap.getString("okButton.text")); // NOI18N
    okButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        okButtonActionPerformed(evt);
      }
    });

    cancelButton.setText(resourceMap.getString("cancelButton.text")); // NOI18N
    cancelButton.setName("cancelButton"); // NOI18N
    cancelButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cancelButtonActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addGroup(layout.createSequentialGroup()
            .addComponent(cancelButton)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(okButton))
          .addComponent(filtersTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton});

    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(filtersTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(cancelButton)
          .addComponent(okButton))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

    private void excludeContainStopWordsCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_excludeContainStopWordsCheckBoxItemStateChanged
        // the rule are: if its filters all the phrase that contains stop words, why bother to filter the phrase that start or end with it.
        if(excludeContainStopWordsCheckBox.isSelected()){
            excludeStartStopWordsCheckBox.setSelected(true);
            excludeEndStopWordsCheckBox.setSelected(true);
            excludeStartStopWordsCheckBox.setEnabled(false);
            excludeEndStopWordsCheckBox.setEnabled(false);
        } else {
            excludeStartStopWordsCheckBox.setEnabled(true);
            excludeEndStopWordsCheckBox.setEnabled(true);
        }
}//GEN-LAST:event_excludeContainStopWordsCheckBoxItemStateChanged

    private void numOfTermFilterMinSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_numOfTermFilterMinSpinnerStateChanged
        // if the minimum number of terms changes,
        int currentMaxValue = ((javax.swing.SpinnerNumberModel) numOfTermFilterMaxSpinner.getModel()).getNumber().intValue();
        int currentMinValue = ((javax.swing.SpinnerNumberModel) numOfTermFilterMinSpinner.getModel()).getNumber().intValue();
        // if its value are greater than the current value of maximum number of words spinner, change the value of that component.
        if(currentMinValue > currentMaxValue){
            numOfTermFilterMaxSpinner.setValue(currentMinValue);
        }
}//GEN-LAST:event_numOfTermFilterMinSpinnerStateChanged

    private void numOfTermFilterMaxSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_numOfTermFilterMaxSpinnerStateChanged
        // if the maximum number of terms changes,
        int currentMaxValue = ((javax.swing.SpinnerNumberModel) numOfTermFilterMaxSpinner.getModel()).getNumber().intValue();
        int currentMinValue = ((javax.swing.SpinnerNumberModel) numOfTermFilterMinSpinner.getModel()).getNumber().intValue();
        // if its value are smaller than the current value of minimum number of words spinner, change the value of that component.
        if(currentMinValue > currentMaxValue){
            numOfTermFilterMinSpinner.setValue(currentMaxValue);
        }
        
        // if the value are zero, set as infinity ∞
        //if (currentMaxValue == 0){
        //    numOfTermFilterMinSpinner.setValue("∞");
        //}
}//GEN-LAST:event_numOfTermFilterMaxSpinnerStateChanged

    private void posTaggerAdvanceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_posTaggerAdvanceButtonActionPerformed
        JFrame mainFrame = NewsClusteringLuceneApp.getApplication().getMainFrame();
        if(posTaggerComboBox.getSelectedIndex() == 0){
            IndPOSTagger indPOSTagger = (IndPOSTagger) ForwardSequentialConfigurationView.extractorConfEdit.posTagger;
            int i = IndPOSTaggerConfigViewDialog.showDialog(mainFrame, true, indPOSTagger);
            if (i == IndPOSTaggerConfigViewDialog.RET_OK){
                ForwardSequentialConfigurationView.extractorConfEdit.posTagger = new IndPOSTagger(IndPOSTaggerConfigViewDialog.getindPOSTaggerConfigEdit());
            }
        }
}//GEN-LAST:event_posTaggerAdvanceButtonActionPerformed

    private void stopWordFileBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopWordFileBrowseButtonActionPerformed
        int r = chooser.showOpenDialog(this);
        if (r != javax.swing.JFileChooser.APPROVE_OPTION) {
            return;
        }
        stopWordFileTextField.setText(chooser.getSelectedFile().getAbsolutePath());
}//GEN-LAST:event_stopWordFileBrowseButtonActionPerformed

    private void usesPOSTCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_usesPOSTCheckBoxItemStateChanged
        // if its not using Part-of-Speech tag in its pattern, why bother to process its POSTag.
        if (usesPOSTCheckBox.isSelected()) {
            posTaggerComboBox.setEnabled(true);
            posTaggerAdvanceButton.setEnabled(true);
            posTagComboBoxModel = new DefaultComboBoxModel(ForwardSequentialConfigurationView.extractorConfEdit.posTagger.getTagSetList());
            posTagComboBox.setModel(posTagComboBoxModel);
            this.seePOSTDescButton.setEnabled(true);
        } else {
            posTaggerComboBox.setEnabled(false);
            posTaggerAdvanceButton.setEnabled(false);
            posTagComboBoxModel.removeAllElements();
            this.seePOSTDescButton.setEnabled(false);
        }
}//GEN-LAST:event_usesPOSTCheckBoxItemStateChanged

    private void mergeOverlapPhrasesCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_mergeOverlapPhrasesCheckBoxItemStateChanged
        if (mergeOverlapPhrasesCheckBox.isSelected()) {
           removeOverlappedPhrasesCheckBox.setEnabled(true);
        } else {
            removeOverlappedPhrasesCheckBox.setSelected(false);
            removeOverlappedPhrasesCheckBox.setEnabled(false);
        }
    }//GEN-LAST:event_mergeOverlapPhrasesCheckBoxItemStateChanged

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed

        // Phrase Pattern Filters (POS Tag)
        ForwardSequentialConfigurationView.extractorConfEdit.usesPOSTagger = usesPOSTCheckBox.isSelected();
        if (!usesPOSTCheckBox.isSelected()) {
            System.out.println("false");
        //    ConfigurationViewDialog.appConfEdit.posTagger = new IndPOSTagger(); // currently its only have a single value
        //} else {
            ForwardSequentialConfigurationView.extractorConfEdit.usesPOSTagger = false;
            ForwardSequentialConfigurationView.extractorConfEdit.posTagger = null;
        } else {
            System.out.println("true");
            ForwardSequentialConfigurationView.extractorConfEdit.usesPOSTagger = true;
        }

        // Phrase Pattern Filters (Pattern)
        ForwardSequentialConfigurationView.extractorConfEdit.patternFilters = new ArrayList<Pattern>();
        Object[] p = patternsListModel.toArray();
        for (Object object : p) {
            ForwardSequentialConfigurationView.extractorConfEdit.patternFilters.add((Pattern) object);
        }

        // Include All Possibilities
        ForwardSequentialConfigurationView.extractorConfEdit.includeAllPossibilities = includeAllPossibilitiesCheckBox.isSelected();

        // Stop Words Filter
        ForwardSequentialConfigurationView.extractorConfEdit.stopWordsFile = new File(stopWordFileTextField.getText());
        ForwardSequentialConfigurationView.extractorConfEdit.excludePhraseContainsStopWord = excludeContainStopWordsCheckBox.isSelected();
        ForwardSequentialConfigurationView.extractorConfEdit.excludePhraseStartedWithStopWord = excludeStartStopWordsCheckBox.isSelected();
        ForwardSequentialConfigurationView.extractorConfEdit.excludePhraseEndedWithStopWord = excludeEndStopWordsCheckBox.isSelected();

        // Num Of Words Filter
        ForwardSequentialConfigurationView.extractorConfEdit.phraseExtractorMaxN = ((Integer) numOfTermFilterMaxSpinner.getModel().getValue()).intValue();
        ForwardSequentialConfigurationView.extractorConfEdit.phraseExtractorMinN = ((Integer) numOfTermFilterMinSpinner.getModel().getValue()).intValue();        // TODO add your handling code here:

        ForwardSequentialConfigurationView.extractorConfEdit.mergeOverlapPhrases = mergeOverlapPhrasesCheckBox.isSelected();
        ForwardSequentialConfigurationView.extractorConfEdit.removeOverlapPhrases = removeOverlappedPhrasesCheckBox.isSelected();

        // close this window
        phraseExtractorConfigurationView.setVisible(false);
        dispose();
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
       // present the previous configuration for the return value
        //close this window frame
        extractorConfEdit = extractorConfBackup;
        phraseExtractorConfigurationView.setVisible(false);
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void elementTypeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elementTypeComboBoxActionPerformed
        PatternElement selectedElement = (PatternElement) elementsList.getSelectedValue();
        if (this.elementTypeComboBox.getSelectedIndex() == 1) { // for type POSTag
            selectedElement.setElementType(PatternElement.ELEMENT_TYPE_POST);
            this.posTagComboBox.setEnabled(true);
            this.posTagComboBox.setVisible(true);
            this.elementStringTextField.setEnabled(false);
        } else if (this.elementTypeComboBox.getSelectedIndex() == 0) { // for type Word
            selectedElement.setElementType(PatternElement.ELEMENT_TYPE_TEXT);
            this.posTagComboBox.setEnabled(false);
            this.posTagComboBox.setVisible(false);
            this.elementStringTextField.setEnabled(true);
        }
        elementsList.repaint();
}//GEN-LAST:event_elementTypeComboBoxActionPerformed

    private void patternsAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_patternsAddButtonActionPerformed
        // new default pattern value
        patternsListModel.addElement(new Pattern(Pattern.PATTERN_TYPE_FREE,
                new PatternElement[] {
            new PatternElement(PatternElement.ELEMENT_TYPE_TEXT, "<Element 1 - insert text here>"),
            new PatternElement(PatternElement.ELEMENT_TYPE_TEXT, "<Element 2 - insert text here>"),
            new PatternElement(PatternElement.ELEMENT_TYPE_TEXT, "<Element 3 - insert text here>")}));
}//GEN-LAST:event_patternsAddButtonActionPerformed

    private void posTagComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_posTagComboBoxItemStateChanged
        if (posTagComboBox.getModel().getSize() > 0){
            PatternElement selectedElement = (PatternElement) elementsList.getSelectedValue();
            String selectedItemString = this.posTagComboBox.getSelectedItem().toString();
            selectedElement.setElementString(selectedItemString);
            this.elementStringTextField.setText(selectedItemString);
            elementsList.repaint();
            patternsList.repaint();
        }
}//GEN-LAST:event_posTagComboBoxItemStateChanged

    private void elementStringTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_elementStringTextFieldKeyTyped
        if (evt.getKeyChar() == '\u0008' // BACKSPACE char --> KeyEvent.VK_BACK_SPACE;
                || evt.getKeyChar() == '\u007f') { //DELETE char --> KeyEvent.VK_DELETE
            ((PatternElement) elementsList.getSelectedValue()).setElementString(this.elementStringTextField.getText());
        } else if (evt.getKeyChar() == ' ' // SPACE char --> KeyEvent.VK_SPACE
                || evt.getKeyChar() == '\n' // NEWLINE char --> KeyEvent.VK_ENTER
                || evt.getKeyChar() == '\t'){ // TAB char
            // show Error
            JOptionPane.showMessageDialog(this,
                    "Pattern Element should not contain any space character.",
                    "Space Character Error",
                    JOptionPane.ERROR_MESSAGE);
            evt.setKeyChar(KeyEvent.CHAR_UNDEFINED);
        } else {
            ((PatternElement) elementsList.getSelectedValue()).setElementString(this.elementStringTextField.getText() + evt.getKeyChar());
        }
        elementsList.repaint();
        patternsList.repaint();
}//GEN-LAST:event_elementStringTextFieldKeyTyped

    private void patternsRemoveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_patternsRemoveButtonActionPerformed
        if (this.patternsList.getSelectedIndex() >= 0) {
            DefaultListModel model = (DefaultListModel) patternsList.getModel();
            model.remove(this.patternsList.getSelectedIndex());
            if((Pattern) patternsList.getSelectedValue() == null){
                // clear the pattern element list since there are no pattern selected
                elementsListModel.clear();
                // disable the pattern element view
                this.elementLabel.setEnabled(false);
                this.elementTypeComboBox.setEnabled(false);
                this.posTagComboBox.setEnabled(false);
                this.elementStringTextField.setEnabled(false);
            }
        }
}//GEN-LAST:event_patternsRemoveButtonActionPerformed

    private void patternTypeExactRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_patternTypeExactRadioButtonActionPerformed
        ((Pattern) patternsList.getSelectedValue()).setPatternType(Pattern.PATTERN_TYPE_EXACT);
        patternsList.repaint();
}//GEN-LAST:event_patternTypeExactRadioButtonActionPerformed

    private void patternsListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_patternsListValueChanged
        Pattern selectedPattern = (Pattern) patternsList.getSelectedValue();
        if(selectedPattern != null){
            this.patternTypeExactRadioButton.setEnabled(true);
            this.patternTypeFreeRadioButton.setEnabled(true);
            if (selectedPattern.getPatternType() == Pattern.PATTERN_TYPE_EXACT) {
                this.patternTypeExactRadioButton.setSelected(true);
            } else if (selectedPattern.getPatternType() == Pattern.PATTERN_TYPE_FREE) {
                this.patternTypeFreeRadioButton.setSelected(true);
            }
            elementsListModel.clear();
            for (PatternElement element : selectedPattern.getElements()) {
                elementsListModel.addElement(element);
            }
            // disable the pattern element view
            this.elementLabel.setEnabled(false);
            this.elementTypeComboBox.setEnabled(false);
            this.posTagComboBox.setEnabled(false);
            this.elementStringTextField.setEnabled(false);
        }
}//GEN-LAST:event_patternsListValueChanged

    private void patternTypeFreeRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_patternTypeFreeRadioButtonActionPerformed
        ((Pattern) patternsList.getSelectedValue()).setPatternType(Pattern.PATTERN_TYPE_FREE);
        patternsList.repaint();
}//GEN-LAST:event_patternTypeFreeRadioButtonActionPerformed

    private void elementsRemoveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elementsRemoveButtonActionPerformed
        if (this.elementsList.getSelectedIndex() >= 0) {
            // first delete the instance of element on the pattern list
            ((Pattern) patternsList.getSelectedValue()).getElements().remove(((PatternElement)this.elementsList.getSelectedValue()));
            // then delete the instance of element on the element list
            ((DefaultListModel) elementsList.getModel()).remove(this.elementsList.getSelectedIndex());
            patternsList.repaint();
            // disable the pattern element view
            this.elementLabel.setEnabled(false);
            this.elementTypeComboBox.setEnabled(false);
            this.posTagComboBox.setEnabled(false);
            this.elementStringTextField.setEnabled(false);
        }
}//GEN-LAST:event_elementsRemoveButtonActionPerformed

    private void elementsAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elementsAddButtonActionPerformed
        PatternElement newElement = new PatternElement(PatternElement.ELEMENT_TYPE_TEXT, "<Element - insert text here>");
        elementsListModel.addElement(newElement);
        ((Pattern) patternsList.getSelectedValue()).getElements().add(newElement);
        elementsList.repaint();
        patternsList.repaint();
}//GEN-LAST:event_elementsAddButtonActionPerformed

    private void elementsListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_elementsListValueChanged

        PatternElement selectedElement = (PatternElement) elementsList.getSelectedValue();
        if(selectedElement != null){
            this.elementLabel.setEnabled(true);
            this.elementTypeComboBox.setEnabled(true);
            if (selectedElement.getElementType() == PatternElement.ELEMENT_TYPE_POST) {
                this.elementTypeComboBox.setSelectedIndex(1);
                this.posTagComboBox.setEnabled(true);
                this.elementStringTextField.setText(selectedElement.getElementString());
                this.posTagComboBox.setSelectedItem(selectedElement.getElementString());
            } else if (selectedElement.getElementType() == PatternElement.ELEMENT_TYPE_TEXT) {
                this.elementTypeComboBox.setSelectedIndex(0);
                this.posTagComboBox.setEnabled(false);
                this.elementStringTextField.setText(selectedElement.getElementString());
            }
        } else {
            this.elementLabel.setEnabled(false);
            this.elementTypeComboBox.setEnabled(false);
        }
}//GEN-LAST:event_elementsListValueChanged

    private void seePOSTDescButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seePOSTDescButtonActionPerformed
        JFrame mainFrame = NewsClusteringLuceneApp.getApplication().getMainFrame();
        POSTagSetView.showDialog(mainFrame, true, ForwardSequentialConfigurationView.extractorConfEdit.posTagger.getTaggerTitle(),
               ForwardSequentialConfigurationView.extractorConfEdit.posTagger.getTagSetListDescription());
}//GEN-LAST:event_seePOSTDescButtonActionPerformed

    private void removeOverlappedPhrasesCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_removeOverlappedPhrasesCheckBoxItemStateChanged
        if (removeOverlappedPhrasesCheckBox.isSelected()) {
            includeAllPossibilitiesCheckBox.setEnabled(false);
            includeAllPossibilitiesCheckBox.setSelected(false);
        } else {
            includeAllPossibilitiesCheckBox.setEnabled(true);
        }
    }//GEN-LAST:event_removeOverlappedPhrasesCheckBoxItemStateChanged

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel SWFilterExcludeLabel;
  private javax.swing.JButton cancelButton;
  private javax.swing.JLabel elementLabel;
  private javax.swing.JTextField elementStringTextField;
  private javax.swing.JComboBox elementTypeComboBox;
  private javax.swing.JButton elementsAddButton;
  private javax.swing.JLabel elementsLabel;
  private javax.swing.JList elementsList;
  private javax.swing.JScrollPane elementsListScrollPane;
  private javax.swing.JButton elementsRemoveButton;
  private javax.swing.JCheckBox excludeContainStopWordsCheckBox;
  private javax.swing.JCheckBox excludeEndStopWordsCheckBox;
  private javax.swing.JCheckBox excludeStartStopWordsCheckBox;
  private javax.swing.JTabbedPane filtersTabbedPane;
  private javax.swing.JCheckBox includeAllPossibilitiesCheckBox;
  private javax.swing.JCheckBox mergeOverlapPhrasesCheckBox;
  private javax.swing.JPanel miscFiltersPanel;
  private javax.swing.JLabel numOfTermFilterMaxLabel;
  private javax.swing.JSpinner numOfTermFilterMaxSpinner;
  private javax.swing.JLabel numOfTermFilterMinLabel;
  private javax.swing.JSpinner numOfTermFilterMinSpinner;
  private javax.swing.JLabel numOfTermFiltersLabel;
  private javax.swing.JSeparator numOfTermFiltersSeparator;
  private javax.swing.JButton okButton;
  private javax.swing.JLabel overlapPhrasesLabel;
  private javax.swing.JSeparator overlapPhrasesSeparator;
  private javax.swing.JPanel patternFiltersPanel;
  private javax.swing.JRadioButton patternTypeExactRadioButton;
  private javax.swing.JRadioButton patternTypeFreeRadioButton;
  private javax.swing.ButtonGroup patternTypebuttonGroup;
  private javax.swing.JButton patternsAddButton;
  private javax.swing.JLabel patternsLabel;
  private javax.swing.JList patternsList;
  private javax.swing.JScrollPane patternsListScrollPane;
  private javax.swing.JButton patternsRemoveButton;
  private javax.swing.JComboBox posTagComboBox;
  private javax.swing.JButton posTaggerAdvanceButton;
  private javax.swing.JComboBox posTaggerComboBox;
  private javax.swing.JCheckBox removeOverlappedPhrasesCheckBox;
  private javax.swing.JButton seePOSTDescButton;
  private javax.swing.JButton stopWordFileBrowseButton;
  private javax.swing.JLabel stopWordFileLabel;
  private javax.swing.JTextField stopWordFileTextField;
  private javax.swing.JLabel stopWordFiltersLabel;
  private javax.swing.JSeparator stopWordFiltersSeparator;
  private javax.swing.JCheckBox usesPOSTCheckBox;
  // End of variables declaration//GEN-END:variables

    private static ForwardSequentialConfigurationView phraseExtractorConfigurationView;
    private javax.swing.JFileChooser chooser;
    private static ForwardSequentialConfiguration extractorConfBackup;
    private static ForwardSequentialConfiguration extractorConfEdit;

    private DefaultListModel patternsListModel = new DefaultListModel();
    private DefaultListModel elementsListModel = new DefaultListModel();
    private DefaultComboBoxModel posTagComboBoxModel = new DefaultComboBoxModel(new String[] { "/NN", "/NNG", "/NNP", "etc..." });
}