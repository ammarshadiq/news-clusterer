/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ConfigurationDialog.java
 *
 * Created on Jan 10, 2011, 11:10:09 AM
 */

package newsclusteringlucene;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import newsclusteringlucene.clusterlabeling.numberofwordscaling.ClusterLabelScalingPoisson;
import newsclusteringlucene.phrasesextractor.ForwardSequential;
import newsclusteringlucene.phrasesextractor.ForwardSequentialConfiguration;
import newsclusteringlucene.phrasesextractor.ForwardSequentialConfigurationView;

/**
 *
 * @author shadiq
 */
public class ConfigurationViewDialog extends javax.swing.JDialog {
    
  //<editor-fold defaultstate="collapsed" desc="general UI rule">
  // the rule is as follows:
  //      if the selected clusterMergingMethod is MERGING_NATURAL_TRESHOLD, then
  //          dendogramCuttingTreshold is ignored and not editable
  //          specifiedNumberOfCluster is ignored and not editable
  //      if the selected clusterMergingMethod is MERGING_SPECIFIED_TRESHOLD, then
  //          dendogramCuttingTreshold is editable
  //          specifiedNumberOfCluster is ignored and not editable
  //      if the selected clusterMergingMethod is MERGING_SPECIFIED_NUMBER_OF_CLUSTER_TRESHOLD, then
  //          dendogramCuttingTreshold is ignored and not editable
  //          specifiedNumberOfCluster is editable
  //</editor-fold>
  
  /** Creates new form ConfigurationDialog */
  public ConfigurationViewDialog(java.awt.Frame parent, boolean modal, Configuration appConf) {
    super(parent, modal);
    initComponents();
    setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
    chooser.setCurrentDirectory(new File("."));

    appConfEdit = appConf;
    appConfBackup = appConf.clone();

    // INDEX FILE
    if (appConfEdit.luceneIndexFolder != null) {
      applyButton.setEnabled(true);
      indexFileTextField.setText(appConfEdit.luceneIndexFolder.getAbsolutePath());
    } else {
      applyButton.setEnabled(false);
    }
    
    if (appConfEdit.useURLFilter){
        filterURLOnClusteringCheckBox.setSelected(true);
        filterURLFileTextField.setEnabled(true);
        filterURLFileBrowseButton.setEnabled(true);
      } else {
      filterURLOnClusteringCheckBox.setSelected(false);
        filterURLFileTextField.setEnabled(false);
        filterURLFileBrowseButton.setEnabled(false);
      }
    
    if (appConfEdit.URLFilterFile != null) {
      filterURLFileTextField.setText(appConfEdit.URLFilterFile.getAbsolutePath());
    }
    
    if(appConfEdit.useURLFilter){
      filterURLFileTextField.setEnabled(true);
      filterURLFileBrowseButton.setEnabled(true);
    } else {
      filterURLFileTextField.setEnabled(false);
      filterURLFileBrowseButton.setEnabled(false);
    }

    // CLUSTERING
    dendogramCuttingTresholdSpinner.getModel().setValue(appConfEdit.dendogramCuttingTreshold);
    numberOfClusterSpinner.setValue(appConfEdit.specifiedNumberOfCluster);
    if (appConfEdit.clusterMergingMethod == Configuration.MERGING_SPECIFIED_TRESHOLD) { // specified cutting treshold
      clusterMergingMethodComboBox.setSelectedIndex(0);
      dendogramCuttingTresholdLabel.setEnabled(true);
      dendogramCuttingTresholdSlider.setEnabled(true);
      dendogramCuttingTresholdSpinner.setEnabled(true);
      numberOfClusterLabel.setEnabled(false);
      numberOfClusterSpinner.setEnabled(false);
      ManualClusterFileLabel.setEnabled(false);
      ManualClusterFileTextField.setEnabled(false);
      ManualClusterFileBrowseButton.setEnabled(false);
    } else if (appConfEdit.clusterMergingMethod == Configuration.MERGING_NATURAL_TRESHOLD) { // natural cutting treshold
      clusterMergingMethodComboBox.setSelectedIndex(1);
      dendogramCuttingTresholdLabel.setEnabled(false);
      dendogramCuttingTresholdSlider.setEnabled(false);
      dendogramCuttingTresholdSpinner.setEnabled(false);
      numberOfClusterLabel.setEnabled(false);
      numberOfClusterSpinner.setEnabled(false);
      ManualClusterFileLabel.setEnabled(false);
      ManualClusterFileTextField.setEnabled(false);
      ManualClusterFileBrowseButton.setEnabled(false);
    } else if (appConfEdit.clusterMergingMethod == Configuration.MERGING_SPECIFIED_NUMBER_OF_CLUSTER_TRESHOLD) { // specified cluster number
      clusterMergingMethodComboBox.setSelectedIndex(2);
      dendogramCuttingTresholdLabel.setEnabled(false);
      dendogramCuttingTresholdSlider.setEnabled(false);
      dendogramCuttingTresholdSpinner.setEnabled(false);
      numberOfClusterLabel.setEnabled(true);
      numberOfClusterSpinner.setEnabled(true);
      ManualClusterFileLabel.setEnabled(false);
      ManualClusterFileTextField.setEnabled(false);
      ManualClusterFileBrowseButton.setEnabled(false);
    } else if (appConfEdit.clusterMergingMethod == Configuration.MERGING_MANUAL_BY_TOPIC) { // manual by topic
      clusterMergingMethodComboBox.setSelectedIndex(3);
        dendogramCuttingTresholdLabel.setEnabled(false);
        dendogramCuttingTresholdSlider.setEnabled(false);
        dendogramCuttingTresholdSpinner.setEnabled(false);
        numberOfClusterLabel.setEnabled(false);
        numberOfClusterSpinner.setEnabled(false);
        ManualClusterFileLabel.setEnabled(true);
        ManualClusterFileTextField.setEnabled(true);
        ManualClusterFileBrowseButton.setEnabled(true);
    }

    if (appConfEdit.clusteringLogBase == Configuration.LOG_BASE_E) {
      clusterMergingLogBaseComboBox.setSelectedIndex(0);
    } else if (appConfEdit.clusteringLogBase == Configuration.LOG_BASE_10) {
      clusterMergingLogBaseComboBox.setSelectedIndex(1);
    } else if (appConfEdit.clusteringLogBase == Configuration.LOG_BASE_2) {
      clusterMergingLogBaseComboBox.setSelectedIndex(2);
    }
    
    if (appConfEdit.manualTopicFile != null) {
      ManualClusterFileTextField.setText(appConfEdit.manualTopicFile.getAbsolutePath());
    }

    //STOP WORDS
    if (appConfEdit.stopWordsFile != null) {
      stopWordFileTextField.setText(appConfEdit.stopWordsFile.getAbsolutePath());
    }
    stopWordFileTextField.setText(appConfEdit.stopWordsFile.getAbsolutePath());
    discardStopWordsOnClusteringCheckBox.setSelected(appConfEdit.discardStopWords);
    if (appConfEdit.discardStopWords) {
      discardStopWordsOnClusteringCheckBox.setSelected(true);
      stopWordFileLabel.setEnabled(true);
      stopWordFileTextField.setEnabled(true);
      stopWordFileBrowseButton.setEnabled(true);
    } else {
      discardStopWordsOnClusteringCheckBox.setSelected(false);
      stopWordFileLabel.setEnabled(false);
      stopWordFileTextField.setEnabled(false);
      stopWordFileBrowseButton.setEnabled(false);
    }

    // PHRASES EXTRACTOR
    if (appConfEdit.phraseExtractorMethod == Configuration.PHRASES_EXTRACTOR_FORWARD_SEQUENTIAL) {
      phraseExtractorComboBox.setSelectedIndex(0);
    } else if (appConfEdit.phraseExtractorMethod == Configuration.PHRASES_EXTRACTOR_SIMPLE) {
      phraseExtractorComboBox.setSelectedIndex(1);
    }

    if (appConfEdit.clusterLabelingLogBase == Configuration.LOG_BASE_E) {
      labelingLogBaseComboBox.setSelectedIndex(0);
    } else if (appConfEdit.clusterLabelingLogBase == Configuration.LOG_BASE_10) {
      labelingLogBaseComboBox.setSelectedIndex(1);
    } else if (appConfEdit.clusterLabelingLogBase == Configuration.LOG_BASE_2) {
      labelingLogBaseComboBox.setSelectedIndex(2);
    }

    // FREQUENT PHRASES FILTER
    fpfNumDocMinSpinner.getModel().setValue(appConfEdit.frequentPhraseFilterNumDocMin);
    fpfPersDocMinSpinner.getModel().setValue(appConfEdit.frequentPhraseFilterPersentDocMin);
    fpfNumDocMaxSpinner.getModel().setValue(appConfEdit.frequentPhraseFilterNumDocMax);
    fpfPersDocMaxSpinner.getModel().setValue(appConfEdit.frequentPhraseFilterPersentDocMax);

    // LABELING METHODS
    if (appConfEdit.clusterLabelingMethod == Configuration.LABELING_MUTUAL_INFORMATION) {
      labelingMethodComboBox.setSelectedIndex(0);
    } else if (appConfEdit.clusterLabelingMethod == Configuration.LABELING_CHI_SQUARE_SELECTION) {
      labelingMethodComboBox.setSelectedIndex(1);
      // disable log base
    } else if (appConfEdit.clusterLabelingMethod == Configuration.LABELING_RANK_D_SCORE) {
      labelingMethodComboBox.setSelectedIndex(2);
    } else if (appConfEdit.clusterLabelingMethod == Configuration.LABELING_MAX_TFIDF_BY_CLUSTER) {
      labelingMethodComboBox.setSelectedIndex(3);
    } else if (appConfEdit.clusterLabelingMethod == Configuration.LABELING_MAX_TFIDF_BY_COLLECTION) {
      labelingMethodComboBox.setSelectedIndex(4);
    }

    // LABEL SCALING METHODS
    if (appConfEdit.clusterLabelingScalingMethod == Configuration.LABELING_SCALING_NONE) {
      labelingScalingMethodComboBox.setSelectedIndex(0);
      ClusterLabelingMethodPoissonLabel.setEnabled(false);
      ClusterLabelingMethodPoissonSpinner.setEnabled(false);
    } else if (appConfEdit.clusterLabelingScalingMethod == Configuration.LABELING_SCALING_POISSON) {
      labelingScalingMethodComboBox.setSelectedIndex(1);
      ClusterLabelingMethodPoissonLabel.setEnabled(true);
      ClusterLabelingMethodPoissonSpinner.setEnabled(true);
    }

    ClusterLabelingMethodPoissonSpinner.getModel().setValue(appConfEdit.poissonScalingLambda);
  }

  public static Configuration showDialog(java.awt.Frame parent, boolean modal,
          Configuration appConf) {
    configurationViewDialog = new ConfigurationViewDialog(parent, modal, appConf);
    configurationViewDialog.setLocationRelativeTo(parent);
    configurationViewDialog.setVisible(true);
    return appConfEdit;
  }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    dendogramCuttingTresholdLabel = new javax.swing.JLabel();
    dendogramCuttingTresholdSpinner = new javax.swing.JSpinner();
    labelingScalingMethodLabel = new javax.swing.JLabel();
    applyButton = new javax.swing.JButton();
    labelingScalingMethodComboBox = new javax.swing.JComboBox();
    phraseExtractorLabel = new javax.swing.JLabel();
    phraseExtractorComboBox = new javax.swing.JComboBox();
    okButton = new javax.swing.JButton();
    cancelButton = new javax.swing.JButton();
    clusterLabelingMethodSeparator = new javax.swing.JSeparator();
    phrasesExtractorSeparator = new javax.swing.JSeparator();
    ClusterLabelingMethodPoissonSpinner = new javax.swing.JSpinner();
    ClusterLabelingMethodPoissonLabel = new javax.swing.JLabel();
    labelingMethodLabel = new javax.swing.JLabel();
    labelingMethodComboBox = new javax.swing.JComboBox();
    dendogramCuttingTresholdSlider = new javax.swing.JSlider();
    clusterMergingMethodLabel = new javax.swing.JLabel();
    clusterMergingMethodComboBox = new javax.swing.JComboBox();
    numberOfClusterLabel = new javax.swing.JLabel();
    numberOfClusterSpinner = new javax.swing.JSpinner();
    okCancelApplyButtonSeparator = new javax.swing.JSeparator();
    clusteringMethodLabel = new javax.swing.JLabel();
    phrasesExtractorLabel = new javax.swing.JLabel();
    clusterLabelingMethodLabel = new javax.swing.JLabel();
    clusteringMethodSeparator = new javax.swing.JSeparator();
    phraseExtractorConfigureButton = new javax.swing.JButton();
    frequentPhrasesFilterLabel = new javax.swing.JLabel();
    fpfDocLabel = new javax.swing.JLabel();
    clusterMergingLogBaseLabel = new javax.swing.JLabel();
    clusterMergingLogBaseComboBox = new javax.swing.JComboBox();
    labelingLogTypeLabel = new javax.swing.JLabel();
    labelingLogBaseComboBox = new javax.swing.JComboBox();
    fpfNumDocMinSpinner = new javax.swing.JSpinner();
    fpfDocMinLabel = new javax.swing.JLabel();
    fpfNumDocMinLabel = new javax.swing.JLabel();
    fpfPersDocMinSpinner = new javax.swing.JSpinner();
    fpfPersDocMinLabel = new javax.swing.JLabel();
    fpfPersDocMaxLabel = new javax.swing.JLabel();
    fpfPersDocMaxSpinner = new javax.swing.JSpinner();
    fpfNumDocMaxSpinner = new javax.swing.JSpinner();
    fpfDocMaxLabel = new javax.swing.JLabel();
    fpfNumDocMaxLabel = new javax.swing.JLabel();
    discardStopWordsOnClusteringCheckBox = new javax.swing.JCheckBox();
    stopWordFileTextField = new javax.swing.JTextField();
    stopWordFileLabel = new javax.swing.JLabel();
    stopWordFileBrowseButton = new javax.swing.JButton();
    indexFileLabel = new javax.swing.JLabel();
    indexFileTitleLabel = new javax.swing.JLabel();
    indexFileSeparator = new javax.swing.JSeparator();
    indexFileTextField = new javax.swing.JTextField();
    filterURLFileTextField = new javax.swing.JTextField();
    indexFileBrowseButton = new javax.swing.JButton();
    filterURLFileBrowseButton = new javax.swing.JButton();
    filterURLOnClusteringCheckBox = new javax.swing.JCheckBox();
    ManualClusterFileLabel = new javax.swing.JLabel();
    ManualClusterFileTextField = new javax.swing.JTextField();
    ManualClusterFileBrowseButton = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(newsclusteringlucene.NewsClusteringLuceneApp.class).getContext().getResourceMap(ConfigurationViewDialog.class);
    setTitle(resourceMap.getString("Form.title")); // NOI18N
    setModal(true);
    setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);
    setResizable(false);

    dendogramCuttingTresholdLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    dendogramCuttingTresholdLabel.setText(resourceMap.getString("dendogramCuttingTresholdLabel.text")); // NOI18N
    dendogramCuttingTresholdLabel.setName("dendogramCuttingTresholdLabel"); // NOI18N

    dendogramCuttingTresholdSpinner.setModel(new javax.swing.SpinnerNumberModel(0.175d, 0.0d, 1.0d, 0.0010d));
    dendogramCuttingTresholdSpinner.setName("dendogramCuttingTresholdSpinner"); // NOI18N
    dendogramCuttingTresholdSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        dendogramCuttingTresholdSpinnerStateChanged(evt);
      }
    });

    labelingScalingMethodLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    labelingScalingMethodLabel.setText(resourceMap.getString("labelingScalingMethodLabel.text")); // NOI18N
    labelingScalingMethodLabel.setName("labelingScalingMethodLabel"); // NOI18N

    applyButton.setText(resourceMap.getString("applyButton.text")); // NOI18N
    applyButton.setName("applyButton"); // NOI18N
    applyButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        applyButtonActionPerformed(evt);
      }
    });

    labelingScalingMethodComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "--None--", "Number of Words Poisson Scaling" }));
    labelingScalingMethodComboBox.setName("labelingScalingMethodComboBox"); // NOI18N
    labelingScalingMethodComboBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        labelingScalingMethodComboBoxActionPerformed(evt);
      }
    });

    phraseExtractorLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    phraseExtractorLabel.setText(resourceMap.getString("phraseExtractorLabel.text")); // NOI18N
    phraseExtractorLabel.setName("phraseExtractorLabel"); // NOI18N

    phraseExtractorComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Forward Sequential", "Simple Extractor" }));
    phraseExtractorComboBox.setName("phraseExtractorComboBox"); // NOI18N
    phraseExtractorComboBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        phraseExtractorComboBoxActionPerformed(evt);
      }
    });

    okButton.setText(resourceMap.getString("okButton.text")); // NOI18N
    okButton.setName("okButton"); // NOI18N
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

    clusterLabelingMethodSeparator.setName("clusterLabelingMethodSeparator"); // NOI18N

    phrasesExtractorSeparator.setName("phrasesExtractorSeparator"); // NOI18N

    ClusterLabelingMethodPoissonSpinner.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(4.0d), null, null, Double.valueOf(0.1d)));
    ClusterLabelingMethodPoissonSpinner.setName("ClusterLabelingMethodPoissonSpinner"); // NOI18N

    ClusterLabelingMethodPoissonLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    ClusterLabelingMethodPoissonLabel.setText(resourceMap.getString("ClusterLabelingMethodPoissonLabel.text")); // NOI18N
    ClusterLabelingMethodPoissonLabel.setName("ClusterLabelingMethodPoissonLabel"); // NOI18N

    labelingMethodLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    labelingMethodLabel.setText(resourceMap.getString("labelingMethodLabel.text")); // NOI18N
    labelingMethodLabel.setName("labelingMethodLabel"); // NOI18N

    labelingMethodComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Mutual Information", "Chi Square Selection", "Rank Feature D-Score", "Max TF-IDF (by Cluster)", "Max TF-IDF (by Collection)" }));
    labelingMethodComboBox.setName("labelingMethodComboBox"); // NOI18N
    labelingMethodComboBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        labelingMethodComboBoxActionPerformed(evt);
      }
    });

    dendogramCuttingTresholdSlider.setFont(resourceMap.getFont("dendogramCuttingTresholdSlider.font")); // NOI18N
    dendogramCuttingTresholdSlider.setMajorTickSpacing(100);
    dendogramCuttingTresholdSlider.setMaximum(1000);
    dendogramCuttingTresholdSlider.setMinorTickSpacing(50);
    dendogramCuttingTresholdSlider.setPaintTicks(true);
    dendogramCuttingTresholdSlider.setValue(825);
    dendogramCuttingTresholdSlider.setName("dendogramCuttingTresholdSlider"); // NOI18N
    dendogramCuttingTresholdSlider.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        dendogramCuttingTresholdSliderStateChanged(evt);
      }
    });

    clusterMergingMethodLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    clusterMergingMethodLabel.setText(resourceMap.getString("clusterMergingMethodLabel.text")); // NOI18N
    clusterMergingMethodLabel.setName("clusterMergingMethodLabel"); // NOI18N

    clusterMergingMethodComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "UPGMA Specified Cutting Treshold", "UPGMA Natural Cutting Treshold", "UPGMA Specified Number of Clusters", "Manual by Topic" }));
    clusterMergingMethodComboBox.setName("clusterMergingMethodComboBox"); // NOI18N
    clusterMergingMethodComboBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        clusterMergingMethodComboBoxActionPerformed(evt);
      }
    });

    numberOfClusterLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    numberOfClusterLabel.setText(resourceMap.getString("numberOfClusterLabel.text")); // NOI18N
    numberOfClusterLabel.setName("numberOfClusterLabel"); // NOI18N
    numberOfClusterLabel.setRequestFocusEnabled(false);
    numberOfClusterLabel.setVerifyInputWhenFocusTarget(false);

    numberOfClusterSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(10), Integer.valueOf(1), null, Integer.valueOf(1)));
    numberOfClusterSpinner.setName("numberOfClusterSpinner"); // NOI18N

    okCancelApplyButtonSeparator.setName("okCancelApplyButtonSeparator"); // NOI18N

    clusteringMethodLabel.setFont(resourceMap.getFont("clusteringMethodLabel.font")); // NOI18N
    clusteringMethodLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    clusteringMethodLabel.setText(resourceMap.getString("clusteringMethodLabel.text")); // NOI18N
    clusteringMethodLabel.setName("clusteringMethodLabel"); // NOI18N

    phrasesExtractorLabel.setFont(resourceMap.getFont("phrasesExtractorLabel.font")); // NOI18N
    phrasesExtractorLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    phrasesExtractorLabel.setText(resourceMap.getString("phrasesExtractorLabel.text")); // NOI18N
    phrasesExtractorLabel.setName("phrasesExtractorLabel"); // NOI18N

    clusterLabelingMethodLabel.setFont(resourceMap.getFont("clusterLabelingMethodLabel.font")); // NOI18N
    clusterLabelingMethodLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    clusterLabelingMethodLabel.setText(resourceMap.getString("clusterLabelingMethodLabel.text")); // NOI18N
    clusterLabelingMethodLabel.setName("clusterLabelingMethodLabel"); // NOI18N

    clusteringMethodSeparator.setName("clusteringMethodSeparator"); // NOI18N

    phraseExtractorConfigureButton.setText(resourceMap.getString("phraseExtractorConfigureButton.text")); // NOI18N
    phraseExtractorConfigureButton.setName("phraseExtractorConfigureButton"); // NOI18N
    phraseExtractorConfigureButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        phraseExtractorConfigureButtonActionPerformed(evt);
      }
    });

    frequentPhrasesFilterLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    frequentPhrasesFilterLabel.setText(resourceMap.getString("frequentPhrasesFilterLabel.text")); // NOI18N
    frequentPhrasesFilterLabel.setName("frequentPhrasesFilterLabel"); // NOI18N

    fpfDocLabel.setFont(resourceMap.getFont("fpfDocLabel.font")); // NOI18N
    fpfDocLabel.setText(resourceMap.getString("fpfDocLabel.text")); // NOI18N
    fpfDocLabel.setName("fpfDocLabel"); // NOI18N

    clusterMergingLogBaseLabel.setText(resourceMap.getString("clusterMergingLogBaseLabel.text")); // NOI18N
    clusterMergingLogBaseLabel.setName("clusterMergingLogBaseLabel"); // NOI18N

    clusterMergingLogBaseComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<html>Log <b><i>e</i></b><html>", "<html>Log <b><i>10</i></b><html>", "<html>Log <b><i>2</i></b><html>" }));
    clusterMergingLogBaseComboBox.setName("clusterMergingLogBaseComboBox"); // NOI18N

    labelingLogTypeLabel.setText(resourceMap.getString("labelingLogTypeLabel.text")); // NOI18N
    labelingLogTypeLabel.setName("labelingLogTypeLabel"); // NOI18N

    labelingLogBaseComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<html>Log <b><i>e</i></b><html>", "<html>Log <b><i>10</i></b><html>", "<html>Log <b><i>2</i></b><html>" }));
    labelingLogBaseComboBox.setName("labelingLogBaseComboBox"); // NOI18N

    fpfNumDocMinSpinner.setFont(resourceMap.getFont("fpfNumDocMinSpinner.font")); // NOI18N
    fpfNumDocMinSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
    fpfNumDocMinSpinner.setName("fpfNumDocMinSpinner"); // NOI18N

    fpfDocMinLabel.setText(resourceMap.getString("fpfDocMinLabel.text")); // NOI18N
    fpfDocMinLabel.setName("fpfDocMinLabel"); // NOI18N

    fpfNumDocMinLabel.setText(resourceMap.getString("fpfNumDocMinLabel.text")); // NOI18N
    fpfNumDocMinLabel.setName("fpfNumDocMinLabel"); // NOI18N

    fpfPersDocMinSpinner.setModel(new javax.swing.SpinnerNumberModel(0.0d, 0.0d, 100.0d, 1.0d));
    fpfPersDocMinSpinner.setName("fpfPersDocMinSpinner"); // NOI18N

    fpfPersDocMinLabel.setText(resourceMap.getString("fpfPersDocMinLabel.text")); // NOI18N
    fpfPersDocMinLabel.setName("fpfPersDocMinLabel"); // NOI18N

    fpfPersDocMaxLabel.setText(resourceMap.getString("fpfPersDocMaxLabel.text")); // NOI18N
    fpfPersDocMaxLabel.setName("fpfPersDocMaxLabel"); // NOI18N

    fpfPersDocMaxSpinner.setModel(new javax.swing.SpinnerNumberModel(0.0d, 0.0d, 100.0d, 1.0d));
    fpfPersDocMaxSpinner.setName("fpfPersDocMaxSpinner"); // NOI18N

    fpfNumDocMaxSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
    fpfNumDocMaxSpinner.setName("fpfNumDocMaxSpinner"); // NOI18N

    fpfDocMaxLabel.setText(resourceMap.getString("fpfDocMaxLabel.text")); // NOI18N
    fpfDocMaxLabel.setName("fpfDocMaxLabel"); // NOI18N

    fpfNumDocMaxLabel.setText(resourceMap.getString("fpfNumDocMaxLabel.text")); // NOI18N
    fpfNumDocMaxLabel.setName("fpfNumDocMaxLabel"); // NOI18N

    discardStopWordsOnClusteringCheckBox.setText(resourceMap.getString("discardStopWordsOnClusteringCheckBox.text")); // NOI18N
    discardStopWordsOnClusteringCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
    discardStopWordsOnClusteringCheckBox.setIconTextGap(0);
    discardStopWordsOnClusteringCheckBox.setName("discardStopWordsOnClusteringCheckBox"); // NOI18N
    discardStopWordsOnClusteringCheckBox.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        discardStopWordsOnClusteringCheckBoxItemStateChanged(evt);
      }
    });

    stopWordFileTextField.setText(resourceMap.getString("stopWordFileTextField.text")); // NOI18N
    stopWordFileTextField.setName("stopWordFileTextField"); // NOI18N

    stopWordFileLabel.setText(resourceMap.getString("stopWordFileLabel.text")); // NOI18N
    stopWordFileLabel.setName("stopWordFileLabel"); // NOI18N

    stopWordFileBrowseButton.setText(resourceMap.getString("stopWordFileBrowseButton.text")); // NOI18N
    stopWordFileBrowseButton.setName("stopWordFileBrowseButton"); // NOI18N
    stopWordFileBrowseButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        stopWordFileBrowseButtonActionPerformed(evt);
      }
    });

    indexFileLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    indexFileLabel.setText(resourceMap.getString("indexFileLabel.text")); // NOI18N
    indexFileLabel.setName("indexFileLabel"); // NOI18N

    indexFileTitleLabel.setFont(resourceMap.getFont("indexFileTitleLabel.font")); // NOI18N
    indexFileTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    indexFileTitleLabel.setText(resourceMap.getString("indexFileTitleLabel.text")); // NOI18N
    indexFileTitleLabel.setName("indexFileTitleLabel"); // NOI18N

    indexFileSeparator.setName("indexFileSeparator"); // NOI18N

    indexFileTextField.setText(resourceMap.getString("indexFileTextField.text")); // NOI18N
    indexFileTextField.setName("indexFileTextField"); // NOI18N

    filterURLFileTextField.setText(resourceMap.getString("filterURLFileTextField.text")); // NOI18N
    filterURLFileTextField.setName("filterURLFileTextField"); // NOI18N

    indexFileBrowseButton.setText(resourceMap.getString("indexFileBrowseButton.text")); // NOI18N
    indexFileBrowseButton.setName("indexFileBrowseButton"); // NOI18N
    indexFileBrowseButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        indexFileBrowseButtonActionPerformed(evt);
      }
    });

    filterURLFileBrowseButton.setText(resourceMap.getString("filterURLFileBrowseButton.text")); // NOI18N
    filterURLFileBrowseButton.setName("filterURLFileBrowseButton"); // NOI18N
    filterURLFileBrowseButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        filterURLFileBrowseButtonActionPerformed(evt);
      }
    });

    filterURLOnClusteringCheckBox.setText(resourceMap.getString("filterURLOnClusteringCheckBox.text")); // NOI18N
    filterURLOnClusteringCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
    filterURLOnClusteringCheckBox.setIconTextGap(0);
    filterURLOnClusteringCheckBox.setName("filterURLOnClusteringCheckBox"); // NOI18N
    filterURLOnClusteringCheckBox.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        filterURLOnClusteringCheckBoxItemStateChanged(evt);
      }
    });

    ManualClusterFileLabel.setText(resourceMap.getString("ManualClusterFileLabel.text")); // NOI18N
    ManualClusterFileLabel.setName("ManualClusterFileLabel"); // NOI18N

    ManualClusterFileTextField.setText(resourceMap.getString("ManualClusterFileTextField.text")); // NOI18N
    ManualClusterFileTextField.setName("ManualClusterFileTextField"); // NOI18N

    ManualClusterFileBrowseButton.setText(resourceMap.getString("ManualClusterFileBrowseButton.text")); // NOI18N
    ManualClusterFileBrowseButton.setName("ManualClusterFileBrowseButton"); // NOI18N
    ManualClusterFileBrowseButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        ManualClusterFileBrowseButtonActionPerformed(evt);
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
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(filterURLOnClusteringCheckBox)
              .addComponent(indexFileLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(indexFileTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
              .addComponent(filterURLFileTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(indexFileBrowseButton))
              .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filterURLFileBrowseButton)))
            .addGap(12, 12, 12))
          .addGroup(layout.createSequentialGroup()
            .addComponent(indexFileTitleLabel)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(indexFileSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE)
            .addContainerGap())
          .addGroup(layout.createSequentialGroup()
            .addComponent(clusteringMethodLabel)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(clusteringMethodSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE)
            .addContainerGap())
          .addGroup(layout.createSequentialGroup()
            .addGap(28, 28, 28)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(ManualClusterFileLabel)
              .addComponent(clusterMergingMethodLabel))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addComponent(clusterMergingMethodComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clusterMergingLogBaseLabel))
              .addComponent(ManualClusterFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(clusterMergingLogBaseComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(ManualClusterFileBrowseButton))
            .addGap(10, 10, 10))
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addComponent(phrasesExtractorLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(phrasesExtractorSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE))
              .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                  .addComponent(frequentPhrasesFilterLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(phraseExtractorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addGroup(layout.createSequentialGroup()
                    .addComponent(phraseExtractorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(phraseExtractorConfigureButton))
                  .addComponent(fpfDocLabel)
                  .addGroup(layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                      .addGroup(layout.createSequentialGroup()
                        .addComponent(fpfDocMaxLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fpfNumDocMaxSpinner))
                      .addGroup(layout.createSequentialGroup()
                        .addComponent(fpfDocMinLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fpfNumDocMinSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(12, 12, 12)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                      .addGroup(layout.createSequentialGroup()
                        .addComponent(fpfNumDocMinLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fpfPersDocMinSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fpfPersDocMinLabel))
                      .addGroup(layout.createSequentialGroup()
                        .addComponent(fpfNumDocMaxLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fpfPersDocMaxSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fpfPersDocMaxLabel)))))
                .addGap(45, 45, 45))
              .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                      .addComponent(discardStopWordsOnClusteringCheckBox)
                      .addComponent(numberOfClusterLabel)
                      .addComponent(dendogramCuttingTresholdLabel))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                      .addComponent(numberOfClusterSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                      .addGroup(layout.createSequentialGroup()
                        .addComponent(stopWordFileLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stopWordFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stopWordFileBrowseButton))))
                  .addGroup(layout.createSequentialGroup()
                    .addGap(192, 192, 192)
                    .addComponent(dendogramCuttingTresholdSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(dendogramCuttingTresholdSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(24, 24, 24))
              .addComponent(okCancelApplyButtonSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 656, Short.MAX_VALUE)
              .addGroup(layout.createSequentialGroup()
                .addComponent(labelingMethodLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(labelingMethodComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelingLogTypeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelingLogBaseComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(layout.createSequentialGroup()
                .addComponent(labelingScalingMethodLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(labelingScalingMethodComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(layout.createSequentialGroup()
                .addComponent(ClusterLabelingMethodPoissonLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(ClusterLabelingMethodPoissonSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(layout.createSequentialGroup()
                .addComponent(clusterLabelingMethodLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clusterLabelingMethodSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE))
              .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(applyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGap(13, 13, 13))))
    );

    layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {applyButton, cancelButton, okButton});

    layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {fpfDocMaxLabel, fpfDocMinLabel});

    layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {fpfNumDocMaxSpinner, fpfNumDocMinSpinner, fpfPersDocMaxSpinner, fpfPersDocMinSpinner});

    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(indexFileTitleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(indexFileSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(indexFileLabel)
          .addComponent(indexFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(indexFileBrowseButton))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(filterURLFileBrowseButton)
          .addComponent(filterURLOnClusteringCheckBox)
          .addComponent(filterURLFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(clusteringMethodLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(clusteringMethodSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(clusterMergingMethodLabel)
          .addComponent(clusterMergingMethodComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(clusterMergingLogBaseLabel)
          .addComponent(clusterMergingLogBaseComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(ManualClusterFileLabel)
          .addComponent(ManualClusterFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(ManualClusterFileBrowseButton))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(stopWordFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(stopWordFileBrowseButton)
          .addComponent(discardStopWordsOnClusteringCheckBox)
          .addComponent(stopWordFileLabel))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(numberOfClusterLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(numberOfClusterSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(dendogramCuttingTresholdSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(dendogramCuttingTresholdSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(dendogramCuttingTresholdLabel))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(phrasesExtractorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(phrasesExtractorSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(phraseExtractorLabel)
          .addComponent(phraseExtractorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(phraseExtractorConfigureButton))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(frequentPhrasesFilterLabel)
          .addComponent(fpfDocLabel))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(fpfDocMinLabel)
          .addComponent(fpfNumDocMinSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(fpfNumDocMinLabel)
          .addComponent(fpfPersDocMinSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(fpfPersDocMinLabel))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(fpfDocMaxLabel)
          .addComponent(fpfNumDocMaxSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(fpfNumDocMaxLabel)
          .addComponent(fpfPersDocMaxSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(fpfPersDocMaxLabel))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(clusterLabelingMethodLabel)
          .addComponent(clusterLabelingMethodSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(labelingMethodLabel)
          .addComponent(labelingMethodComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(labelingLogTypeLabel)
          .addComponent(labelingLogBaseComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(labelingScalingMethodLabel)
          .addComponent(labelingScalingMethodComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(ClusterLabelingMethodPoissonLabel)
          .addComponent(ClusterLabelingMethodPoissonSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(okCancelApplyButtonSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(okButton)
          .addComponent(cancelButton)
          .addComponent(applyButton))
        .addContainerGap())
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

    private void applyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyButtonActionPerformed
      updateData();
      NewsClusteringLuceneView.openAction(appConfEdit.luceneIndexFolder, appConfEdit);
      configurationViewDialog.setVisible(false);
      dispose();
}//GEN-LAST:event_applyButtonActionPerformed

    private void labelingScalingMethodComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_labelingScalingMethodComboBoxActionPerformed
      if (labelingScalingMethodComboBox.getSelectedIndex() == 1) {
        ClusterLabelingMethodPoissonLabel.setEnabled(true);
        ClusterLabelingMethodPoissonSpinner.setEnabled(true);
      } else {
        ClusterLabelingMethodPoissonLabel.setEnabled(false);
        ClusterLabelingMethodPoissonSpinner.setEnabled(false);
      }
}//GEN-LAST:event_labelingScalingMethodComboBoxActionPerformed

    private void phraseExtractorComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phraseExtractorComboBoxActionPerformed
      if (phraseExtractorComboBox.getSelectedIndex() == 0) { // Forward Sequential
        // if its phrases sequential extractor, why bother to generate complete phrase? <question for myself>
        phraseExtractorConfigureButton.setEnabled(true);
      } else if (phraseExtractorComboBox.getSelectedIndex() == 1) { // Simple Extractor
        phraseExtractorConfigureButton.setEnabled(false);
      }
}//GEN-LAST:event_phraseExtractorComboBoxActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
      updateData();
      configurationViewDialog.setVisible(false);
      NewsClusteringLuceneView.openAction(appConfEdit.luceneIndexFolder, appConfEdit);
      dispose();
}//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
      // present the previous configuration for the return value
      //close this window frame
      appConfEdit = appConfBackup;
      configurationViewDialog.setVisible(false);
      dispose();
}//GEN-LAST:event_cancelButtonActionPerformed

    private void dendogramCuttingTresholdSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_dendogramCuttingTresholdSliderStateChanged
      javax.swing.JSlider source = (javax.swing.JSlider) evt.getSource();
      double value = (double) source.getValue();
      double dvalue = Math.abs(value - 1000) / 1000;
      dendogramCuttingTresholdSpinner.getModel().setValue(dvalue);
    }//GEN-LAST:event_dendogramCuttingTresholdSliderStateChanged

    private void clusterMergingMethodComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clusterMergingMethodComboBoxActionPerformed
      if (clusterMergingMethodComboBox.getSelectedIndex() == 0) { // specified cutting treshold
        dendogramCuttingTresholdLabel.setEnabled(true);
        dendogramCuttingTresholdSlider.setEnabled(true);
        dendogramCuttingTresholdSpinner.setEnabled(true);
        numberOfClusterLabel.setEnabled(false);
        numberOfClusterSpinner.setEnabled(false);
        ManualClusterFileLabel.setEnabled(false);
        ManualClusterFileTextField.setEnabled(false);
        ManualClusterFileBrowseButton.setEnabled(false);
      } else if (clusterMergingMethodComboBox.getSelectedIndex() == 1) { // natural cutting treshold
        dendogramCuttingTresholdLabel.setEnabled(false);
        dendogramCuttingTresholdSlider.setEnabled(false);
        dendogramCuttingTresholdSpinner.setEnabled(false);
        numberOfClusterLabel.setEnabled(false);
        numberOfClusterSpinner.setEnabled(false);
        ManualClusterFileLabel.setEnabled(false);
        ManualClusterFileTextField.setEnabled(false);
        ManualClusterFileBrowseButton.setEnabled(false);
      } else if (clusterMergingMethodComboBox.getSelectedIndex() == 2) { // specified cluster number
        dendogramCuttingTresholdLabel.setEnabled(false);
        dendogramCuttingTresholdSlider.setEnabled(false);
        dendogramCuttingTresholdSpinner.setEnabled(false);
        numberOfClusterLabel.setEnabled(true);
        numberOfClusterSpinner.setEnabled(true);
        ManualClusterFileLabel.setEnabled(false);
        ManualClusterFileTextField.setEnabled(false);
        ManualClusterFileBrowseButton.setEnabled(false);
      } else if (clusterMergingMethodComboBox.getSelectedIndex() == 3) { // manual by topic
        dendogramCuttingTresholdLabel.setEnabled(false);
        dendogramCuttingTresholdSlider.setEnabled(false);
        dendogramCuttingTresholdSpinner.setEnabled(false);
        numberOfClusterLabel.setEnabled(false);
        numberOfClusterSpinner.setEnabled(false);
        ManualClusterFileLabel.setEnabled(true);
        ManualClusterFileTextField.setEnabled(true);
        ManualClusterFileBrowseButton.setEnabled(true);
      }
    }//GEN-LAST:event_clusterMergingMethodComboBoxActionPerformed

    private void dendogramCuttingTresholdSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_dendogramCuttingTresholdSpinnerStateChanged
      javax.swing.JSpinner source = (javax.swing.JSpinner) evt.getSource();
      double value = ((Double) source.getModel().getValue()).doubleValue();
      value = 1000 - (value * 1000);
      dendogramCuttingTresholdSlider.setValue((int) Math.round(value));
    }//GEN-LAST:event_dendogramCuttingTresholdSpinnerStateChanged

    private void phraseExtractorConfigureButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phraseExtractorConfigureButtonActionPerformed
      JFrame mainFrame = NewsClusteringLuceneApp.getApplication().getMainFrame();
      if (phraseExtractorComboBox.getSelectedIndex() == Configuration.PHRASES_EXTRACTOR_FORWARD_SEQUENTIAL) {
        ForwardSequentialConfiguration config = ForwardSequentialConfigurationView.showDialog(mainFrame, true,
                ((ForwardSequential) ConfigurationViewDialog.appConfEdit.phrasesExtractor).getExtractorConfig());
        ConfigurationViewDialog.appConfEdit.phrasesExtractor = new ForwardSequential(config);
      }
    }//GEN-LAST:event_phraseExtractorConfigureButtonActionPerformed

    private void discardStopWordsOnClusteringCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_discardStopWordsOnClusteringCheckBoxItemStateChanged
      if (discardStopWordsOnClusteringCheckBox.isSelected()) {
        stopWordFileLabel.setEnabled(true);
        stopWordFileTextField.setEnabled(true);
        stopWordFileBrowseButton.setEnabled(true);
      } else {
        stopWordFileLabel.setEnabled(false);
        stopWordFileTextField.setEnabled(false);
        stopWordFileBrowseButton.setEnabled(false);
      }
    }//GEN-LAST:event_discardStopWordsOnClusteringCheckBoxItemStateChanged

    private void stopWordFileBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopWordFileBrowseButtonActionPerformed
      chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      int r = chooser.showOpenDialog(this);
      if (r != javax.swing.JFileChooser.APPROVE_OPTION) {
        return;
      }
      stopWordFileTextField.setText(chooser.getSelectedFile().getAbsolutePath());
    }//GEN-LAST:event_stopWordFileBrowseButtonActionPerformed

    private void labelingMethodComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_labelingMethodComboBoxActionPerformed
      if(labelingMethodComboBox.getSelectedIndex() == 0) { // mutual information
        labelingLogTypeLabel.setVisible(true);
        labelingLogBaseComboBox.setVisible(true);
        labelingLogBaseComboBox.setSelectedIndex(2); // Select Log Base 2
      } else if (labelingMethodComboBox.getSelectedIndex() == 1) { // chi square selection
        labelingLogTypeLabel.setVisible(false); // disable Log Base Selection, it's not used
        labelingLogBaseComboBox.setVisible(false); // disable Log Base Selection, it's not used
      } else {
        labelingLogTypeLabel.setVisible(true);
        labelingLogBaseComboBox.setVisible(true);
        labelingLogBaseComboBox.setSelectedIndex(0); // Select Log Base e
      }
    }//GEN-LAST:event_labelingMethodComboBoxActionPerformed

    private void filterURLOnClusteringCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_filterURLOnClusteringCheckBoxItemStateChanged
      if (filterURLOnClusteringCheckBox.isSelected()) {
        filterURLFileTextField.setEnabled(true);
        filterURLFileBrowseButton.setEnabled(true);
      } else {
        filterURLFileTextField.setEnabled(false);
        filterURLFileBrowseButton.setEnabled(false);
      }
    }//GEN-LAST:event_filterURLOnClusteringCheckBoxItemStateChanged

    private void filterURLFileBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterURLFileBrowseButtonActionPerformed
      chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      int r = chooser.showOpenDialog(this);
      if (r != javax.swing.JFileChooser.APPROVE_OPTION) {
        return;
      }
      filterURLFileTextField.setText(chooser.getSelectedFile().getAbsolutePath());
    }//GEN-LAST:event_filterURLFileBrowseButtonActionPerformed

    private void indexFileBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_indexFileBrowseButtonActionPerformed
      chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      int r = chooser.showOpenDialog(this);
      if (r != javax.swing.JFileChooser.APPROVE_OPTION) {
        return;
      }
      indexFileTextField.setText(chooser.getSelectedFile().getAbsolutePath());
    }//GEN-LAST:event_indexFileBrowseButtonActionPerformed

    private void ManualClusterFileBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ManualClusterFileBrowseButtonActionPerformed
      chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      int r = chooser.showOpenDialog(this);
      if (r != javax.swing.JFileChooser.APPROVE_OPTION) {
        return;
      }
      ManualClusterFileTextField.setText(chooser.getSelectedFile().getAbsolutePath());
    }//GEN-LAST:event_ManualClusterFileBrowseButtonActionPerformed

  public void enableApplyButton(boolean b) {
    applyButton.setEnabled(b);
  }

  private void updateData() {
    
    // INDEX FILE
    appConfEdit.luceneIndexFolder = new File(indexFileTextField.getText());
    appConfEdit.useURLFilter = filterURLOnClusteringCheckBox.isSelected();
    appConfEdit.URLFilterFile = new File(filterURLFileTextField.getText());
       
    // CLUSTERING
    if (clusterMergingMethodComboBox.getSelectedIndex() == 0) { // specified treshold
      appConfEdit.clusterMergingMethod = Configuration.MERGING_SPECIFIED_TRESHOLD;
      appConfEdit.dendogramCuttingTreshold = ((Double) dendogramCuttingTresholdSpinner.getModel().getValue()).doubleValue();
    } else if (clusterMergingMethodComboBox.getSelectedIndex() == 1) { // natural treshold
      appConfEdit.clusterMergingMethod = Configuration.MERGING_NATURAL_TRESHOLD;
    } else if (clusterMergingMethodComboBox.getSelectedIndex() == 2) { // specified cluster number
      appConfEdit.clusterMergingMethod = Configuration.MERGING_SPECIFIED_NUMBER_OF_CLUSTER_TRESHOLD;
      appConfEdit.specifiedNumberOfCluster = ((javax.swing.SpinnerNumberModel) numberOfClusterSpinner.getModel()).getNumber().intValue();
    } else if (clusterMergingMethodComboBox.getSelectedIndex() == 3) { // manual by topic
      appConfEdit.clusterMergingMethod = Configuration.MERGING_MANUAL_BY_TOPIC;
      appConfEdit.manualTopicFile = new File(ManualClusterFileTextField.getText());
    }

    if (clusterMergingLogBaseComboBox.getSelectedIndex() == 0) { // log base e
      appConfEdit.clusteringLogBase = Configuration.LOG_BASE_E;
    } else if (clusterMergingLogBaseComboBox.getSelectedIndex() == 1) { // log base 10
      appConfEdit.clusteringLogBase = Configuration.LOG_BASE_10;
    } else if (clusterMergingLogBaseComboBox.getSelectedIndex() == 2) { // log base 2
      appConfEdit.clusteringLogBase = Configuration.LOG_BASE_2;
    }

    // STOP WORDS
    appConfEdit.discardStopWords = discardStopWordsOnClusteringCheckBox.isSelected();
    appConfEdit.stopWordsFile = new File(stopWordFileTextField.getText());

    // PHRASE EXTRACTOR
    appConfEdit.phraseExtractorMethod = phraseExtractorComboBox.getSelectedIndex();

    // FREQUENT PHRASES FILTER
    appConfEdit.frequentPhraseFilterNumDocMin = ((javax.swing.SpinnerNumberModel) fpfNumDocMinSpinner.getModel()).getNumber().intValue();
    appConfEdit.frequentPhraseFilterPersentDocMin = ((javax.swing.SpinnerNumberModel) fpfPersDocMinSpinner.getModel()).getNumber().doubleValue();
    appConfEdit.frequentPhraseFilterNumDocMax = ((javax.swing.SpinnerNumberModel) fpfNumDocMaxSpinner.getModel()).getNumber().intValue();
    appConfEdit.frequentPhraseFilterPersentDocMax = ((javax.swing.SpinnerNumberModel) fpfPersDocMaxSpinner.getModel()).getNumber().doubleValue();

    // CLUSTER LABELING
    if (labelingMethodComboBox.getSelectedIndex() == 0){ // mutual information
      appConfEdit.clusterLabelingMethod = Configuration.LABELING_MUTUAL_INFORMATION;
    } else if (labelingMethodComboBox.getSelectedIndex() == 1){ // Chi Square Selection
      appConfEdit.clusterLabelingMethod = Configuration.LABELING_CHI_SQUARE_SELECTION;
    } else if (labelingMethodComboBox.getSelectedIndex() == 2){ // Rank D-Score
      appConfEdit.clusterLabelingMethod = Configuration.LABELING_RANK_D_SCORE;
    } else if (labelingMethodComboBox.getSelectedIndex() == 3){ // Max TF-IDF by Cluster
      appConfEdit.clusterLabelingMethod = Configuration.LABELING_MAX_TFIDF_BY_CLUSTER;
    } else if (labelingMethodComboBox.getSelectedIndex() == 4){ // Max TF-IDF by Cluster
      appConfEdit.clusterLabelingMethod = Configuration.LABELING_MAX_TFIDF_BY_COLLECTION;
    }

    if (labelingLogBaseComboBox.getSelectedIndex() == 0) { // log base e
      appConfEdit.clusterLabelingLogBase = Configuration.LOG_BASE_E;
    } else if (labelingLogBaseComboBox.getSelectedIndex() == 1) { // log base 10
      appConfEdit.clusterLabelingLogBase = Configuration.LOG_BASE_10;
    } else if (labelingLogBaseComboBox.getSelectedIndex() == 2) { // log base 2
      appConfEdit.clusterLabelingLogBase = Configuration.LOG_BASE_2;
    }

    if (labelingScalingMethodComboBox.getSelectedIndex() == 0) { // none
      appConfEdit.clusterLabelingScalingMethod = Configuration.LABELING_SCALING_NONE;
      appConfEdit.clusterLabelScaling = null;
    } else if (labelingScalingMethodComboBox.getSelectedIndex() == 1) { // poisson
      appConfEdit.clusterLabelingScalingMethod = Configuration.LABELING_SCALING_POISSON;
      appConfEdit.clusterLabelScaling = new ClusterLabelScalingPoisson(((Double) ClusterLabelingMethodPoissonSpinner.getModel().getValue()).doubleValue());
    }
  }

  /**
   * get the operation performed after this configuration windows are open
   *  1 = open lucene index operation : means to do all the operation
   *  2 = 
   * @return 
   */
  private int getOperationType(){
    
    
    return 1;
  }
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel ClusterLabelingMethodPoissonLabel;
  private javax.swing.JSpinner ClusterLabelingMethodPoissonSpinner;
  private javax.swing.JButton ManualClusterFileBrowseButton;
  private javax.swing.JLabel ManualClusterFileLabel;
  private javax.swing.JTextField ManualClusterFileTextField;
  private javax.swing.JButton applyButton;
  private javax.swing.JButton cancelButton;
  private javax.swing.JLabel clusterLabelingMethodLabel;
  private javax.swing.JSeparator clusterLabelingMethodSeparator;
  private javax.swing.JComboBox clusterMergingLogBaseComboBox;
  private javax.swing.JLabel clusterMergingLogBaseLabel;
  private javax.swing.JComboBox clusterMergingMethodComboBox;
  private javax.swing.JLabel clusterMergingMethodLabel;
  private javax.swing.JLabel clusteringMethodLabel;
  private javax.swing.JSeparator clusteringMethodSeparator;
  private javax.swing.JLabel dendogramCuttingTresholdLabel;
  private javax.swing.JSlider dendogramCuttingTresholdSlider;
  private javax.swing.JSpinner dendogramCuttingTresholdSpinner;
  private javax.swing.JCheckBox discardStopWordsOnClusteringCheckBox;
  private javax.swing.JButton filterURLFileBrowseButton;
  private javax.swing.JTextField filterURLFileTextField;
  private javax.swing.JCheckBox filterURLOnClusteringCheckBox;
  private javax.swing.JLabel fpfDocLabel;
  private javax.swing.JLabel fpfDocMaxLabel;
  private javax.swing.JLabel fpfDocMinLabel;
  private javax.swing.JLabel fpfNumDocMaxLabel;
  private javax.swing.JSpinner fpfNumDocMaxSpinner;
  private javax.swing.JLabel fpfNumDocMinLabel;
  private javax.swing.JSpinner fpfNumDocMinSpinner;
  private javax.swing.JLabel fpfPersDocMaxLabel;
  private javax.swing.JSpinner fpfPersDocMaxSpinner;
  private javax.swing.JLabel fpfPersDocMinLabel;
  private javax.swing.JSpinner fpfPersDocMinSpinner;
  private javax.swing.JLabel frequentPhrasesFilterLabel;
  private javax.swing.JButton indexFileBrowseButton;
  private javax.swing.JLabel indexFileLabel;
  private javax.swing.JSeparator indexFileSeparator;
  private javax.swing.JTextField indexFileTextField;
  private javax.swing.JLabel indexFileTitleLabel;
  private javax.swing.JComboBox labelingLogBaseComboBox;
  private javax.swing.JLabel labelingLogTypeLabel;
  private javax.swing.JComboBox labelingMethodComboBox;
  private javax.swing.JLabel labelingMethodLabel;
  private javax.swing.JComboBox labelingScalingMethodComboBox;
  private javax.swing.JLabel labelingScalingMethodLabel;
  private javax.swing.JLabel numberOfClusterLabel;
  private javax.swing.JSpinner numberOfClusterSpinner;
  private javax.swing.JButton okButton;
  private javax.swing.JSeparator okCancelApplyButtonSeparator;
  private javax.swing.JComboBox phraseExtractorComboBox;
  private javax.swing.JButton phraseExtractorConfigureButton;
  private javax.swing.JLabel phraseExtractorLabel;
  private javax.swing.JLabel phrasesExtractorLabel;
  private javax.swing.JSeparator phrasesExtractorSeparator;
  private javax.swing.JButton stopWordFileBrowseButton;
  private javax.swing.JLabel stopWordFileLabel;
  private javax.swing.JTextField stopWordFileTextField;
  // End of variables declaration//GEN-END:variables
  private static ConfigurationViewDialog configurationViewDialog;
  private JFileChooser chooser = new JFileChooser();
  private Configuration appConfBackup;
  private static Configuration appConfEdit;
}
