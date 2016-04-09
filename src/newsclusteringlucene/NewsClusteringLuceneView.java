/*
 * NewsClusteringLuceneView.java
 */

package newsclusteringlucene;


import java.awt.event.WindowEvent;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import newsclusteringlucene.clusterlabeling.ClusterLabeling;
import newsclusteringlucene.clusterlabeling.ClusterLabelingChiSquareSelection;
import newsclusteringlucene.clusterlabeling.ClusterLabelingRankFeatureDScore;
import newsclusteringlucene.clusterlabeling.ClusterLabelingMutualInformation;
import newsclusteringlucene.clusterlabeling.ClusterLabelingMaxTFIDFByCluster;
import newsclusteringlucene.clusterlabeling.ClusterLabelingMaxTFIDFByCollection;
import newsclusteringlucene.clustermerging.ClusterMerging;
import newsclusteringlucene.clustermerging.ClusterMergingManualByTopic;
import newsclusteringlucene.clustermerging.ClusterMergingUPGMANaturalTreshold;
import newsclusteringlucene.clustermerging.ClusterMergingUPGMASpecifiedClusterNumber;
import newsclusteringlucene.clustermerging.ClusterMergingUPGMASpecifiedTreshold;
import newsclusteringlucene.graph.DistanceGraph;
import newsclusteringlucene.graph.Vertex;
import newsclusteringlucene.population.LuceneIndexDocument;
import newsclusteringlucene.utils.Cluster;
import newsclusteringlucene.utils.ClusterStat;
import newsclusteringlucene.utils.CollectionStat;
import newsclusteringlucene.utils.DistanceCalculator;
import newsclusteringlucene.utils.CosineSimilarityCalc;
import newsclusteringlucene.utils.LuceneDistanceGraphGenerator;
import newsclusteringlucene.utils.LuceneIndexParser;
import newsclusteringlucene.utils.TimingUtil;
import newsclusteringlucene.evaluation.EvaluationViewDialog;

/**
 * The application's main frame.
 */
public class NewsClusteringLuceneView extends FrameView implements WindowListener {

  public NewsClusteringLuceneView(SingleFrameApplication app) {
    super(app);

    initComponents();


    // status bar initialization - message timeout, idle icon and busy animation, etc
    ResourceMap resourceMap = getResourceMap();

    this.getFrame().setIconImage(
            resourceMap.getImageIcon("NewsFavIcon.cluster").getImage());

    int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
    messageTimer = new Timer(messageTimeout, new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        statusMessageLabel.setText("");
      }
    });
    messageTimer.setRepeats(false);
    int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
    for (int i = 0; i < busyIcons.length; i++) {
      busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
    }
    busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
        statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
      }
    });
    idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
    statusAnimationLabel.setIcon(idleIcon);
    progressBar.setVisible(false);

    // connecting action tasks to status bar via TaskMonitor
    TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
    taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

      public void propertyChange(java.beans.PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if ("started".equals(propertyName)) {
          if (!busyIconTimer.isRunning()) {
            statusAnimationLabel.setIcon(busyIcons[0]);
            busyIconIndex = 0;
            busyIconTimer.start();
          }
          progressBar.setVisible(true);
          progressBar.setIndeterminate(true);
        } else if ("done".equals(propertyName)) {
          busyIconTimer.stop();
          statusAnimationLabel.setIcon(idleIcon);
          progressBar.setVisible(false);
          progressBar.setValue(0);
        } else if ("message".equals(propertyName)) {
          String text = (String) (evt.getNewValue());
          statusMessageLabel.setText((text == null) ? "" : text);
          messageTimer.restart();
        } else if ("progress".equals(propertyName)) {
          int value = (Integer) (evt.getNewValue());
          progressBar.setVisible(true);
          progressBar.setIndeterminate(false);
          progressBar.setValue(value);
        }
      }
    });
  }

  @Action
  public void showAboutBox() {
    if (aboutBox == null) {
      JFrame mainFrame = NewsClusteringLuceneApp.getApplication().getMainFrame();
      aboutBox = new NewsClusteringLuceneAboutBox(mainFrame);
      aboutBox.setLocationRelativeTo(mainFrame);
    }
    NewsClusteringLuceneApp.getApplication().show(aboutBox);
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    chooser = new JFileChooser();
    mainPanel = new javax.swing.JPanel();
    ExplorerSplitPane = new javax.swing.JSplitPane();
    clusterThreeScrollPane = new javax.swing.JScrollPane();
    newsTree = new javax.swing.JTree();
    detailScrollPane = new javax.swing.JScrollPane();
    detailEditorPane = new javax.swing.JEditorPane();
    openAndConfigurationButton = new javax.swing.JButton();
    evaluationButton = new javax.swing.JButton();
    menuBar = new javax.swing.JMenuBar();
    javax.swing.JMenu fileMenu = new javax.swing.JMenu();
    javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
    editMenu = new javax.swing.JMenu();
    configurationMenuItem = new javax.swing.JMenuItem();
    javax.swing.JMenu helpMenu = new javax.swing.JMenu();
    javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
    statusPanel = new javax.swing.JPanel();
    javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
    statusMessageLabel = new javax.swing.JLabel();
    statusAnimationLabel = new javax.swing.JLabel();
    progressBar = new javax.swing.JProgressBar();

    chooser.setCurrentDirectory(new File("."));
    mainPanel.setName("mainPanel"); // NOI18N

    ExplorerSplitPane.setBorder(null);
    ExplorerSplitPane.setName("ExplorerSplitPane"); // NOI18N

    clusterThreeScrollPane.setName("clusterThreeScrollPane"); // NOI18N

    javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
    newsTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
    newsTree.setCellRenderer(new newsclusteringlucene.NewsTreeCellRenderer());
    newsTree.setName("newsTree"); // NOI18N
    newsTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
      public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
        newsTreeValueChanged(evt);
      }
    });
    clusterThreeScrollPane.setViewportView(newsTree);

    ExplorerSplitPane.setLeftComponent(clusterThreeScrollPane);

    detailScrollPane.setName("detailScrollPane"); // NOI18N

    org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(newsclusteringlucene.NewsClusteringLuceneApp.class).getContext().getResourceMap(NewsClusteringLuceneView.class);
    detailEditorPane.setContentType(resourceMap.getString("detailEditorPane.contentType")); // NOI18N
    detailEditorPane.setText(resourceMap.getString("detailEditorPane.text")); // NOI18N
    detailEditorPane.setName("detailEditorPane"); // NOI18N
    detailScrollPane.setViewportView(detailEditorPane);

    ExplorerSplitPane.setRightComponent(detailScrollPane);

    openAndConfigurationButton.setText(resourceMap.getString("openAndConfigurationButton.text")); // NOI18N
    openAndConfigurationButton.setName("openAndConfigurationButton"); // NOI18N
    openAndConfigurationButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        configurationMenuItemActionPerformed(evt);
      }
    });

    evaluationButton.setText(resourceMap.getString("evaluationButton.text")); // NOI18N
    evaluationButton.setName("evaluationButton"); // NOI18N
    evaluationButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        evaluationButtonActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
    mainPanel.setLayout(mainPanelLayout);
    mainPanelLayout.setHorizontalGroup(
      mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(mainPanelLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(ExplorerSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE)
          .addGroup(mainPanelLayout.createSequentialGroup()
            .addComponent(openAndConfigurationButton)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(evaluationButton)))
        .addContainerGap())
    );
    mainPanelLayout.setVerticalGroup(
      mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(mainPanelLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(openAndConfigurationButton)
          .addComponent(evaluationButton))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(ExplorerSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE))
    );

    menuBar.setName("menuBar"); // NOI18N

    fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
    fileMenu.setName("fileMenu"); // NOI18N

    javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(newsclusteringlucene.NewsClusteringLuceneApp.class).getContext().getActionMap(NewsClusteringLuceneView.class, this);
    exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
    exitMenuItem.setName("exitMenuItem"); // NOI18N
    fileMenu.add(exitMenuItem);

    menuBar.add(fileMenu);

    editMenu.setText(resourceMap.getString("editMenu.text")); // NOI18N
    editMenu.setName("editMenu"); // NOI18N

    configurationMenuItem.setText(resourceMap.getString("configurationMenuItem.text")); // NOI18N
    configurationMenuItem.setName("configurationMenuItem"); // NOI18N
    configurationMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        configurationMenuItemActionPerformed(evt);
      }
    });
    editMenu.add(configurationMenuItem);

    menuBar.add(editMenu);

    helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
    helpMenu.setName("helpMenu"); // NOI18N

    aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
    aboutMenuItem.setName("aboutMenuItem"); // NOI18N
    helpMenu.add(aboutMenuItem);

    menuBar.add(helpMenu);

    statusPanel.setName("statusPanel"); // NOI18N

    statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

    statusMessageLabel.setName("statusMessageLabel"); // NOI18N

    statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

    progressBar.setName("progressBar"); // NOI18N

    javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
    statusPanel.setLayout(statusPanelLayout);
    statusPanelLayout.setHorizontalGroup(
      statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 619, Short.MAX_VALUE)
      .addGroup(statusPanelLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(statusMessageLabel)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 435, Short.MAX_VALUE)
        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(statusAnimationLabel)
        .addContainerGap())
    );
    statusPanelLayout.setVerticalGroup(
      statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(statusPanelLayout.createSequentialGroup()
        .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(statusMessageLabel)
          .addComponent(statusAnimationLabel)
          .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(3, 3, 3))
    );

    setComponent(mainPanel);
    setMenuBar(menuBar);
    setStatusBar(statusPanel);
  }// </editor-fold>//GEN-END:initComponents

    private void configurationMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_configurationMenuItemActionPerformed
      JFrame mainFrame = NewsClusteringLuceneApp.getApplication().getMainFrame();
      appConf = ConfigurationViewDialog.showDialog(mainFrame, true, appConf);
    }//GEN-LAST:event_configurationMenuItemActionPerformed

    private void newsTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_newsTreeValueChanged
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) newsTree.getLastSelectedPathComponent();
      if (node != null) {
        String namaObjectPilihan = node.getUserObject().getClass().getName();
        if (namaObjectPilihan.matches("newsclusteringlucene.population.LuceneIndexDocument")) {
          newsclusteringlucene.population.LuceneIndexDocument pop = (newsclusteringlucene.population.LuceneIndexDocument) node.getUserObject();
          StringBuilder sb = new StringBuilder();
          sb.append("<html><head><title>").append(pop.getTitle()).append("</title></head><body>").
                  append("<font size=7>").append(pop.getTitle()).append("</font><br>").
                  append("<font size=3><table border =0>").
                  append("<tr><td colspan=\"2\"><b>URL:</b> ").append(pop.getUrl()).append("</td></tr>").
                  append("<tr><td><b>Date Time:</b> ").append(pop.getDateTime()).append("</td><td><b>Time Stamp :</b> ").append(pop.getTimestamp()).append("</td></tr>").
                  append("<tr><td><b>Reporter:</b> ").append(pop.getReporter()).append("</td><td><b>Location:</b> ").append(pop.getLocation()).append("</td></tr>").
                  append("<tr><td colspan=\"2\"><b>KeyWords:</b> ").append(pop.getKeyWords()).append("</td></tr>").
                  append("<tr><td colspan=\"2\"><b>Short Description:</b> ").append(pop.getShortDescription()).append("</td></tr>").
                  append("<tr><td align=\"center\"  colspan=\"2\">").append("<img src=\"").append(pop.getPictureURL()).append("\"></td></tr>").
                  append("<tr><td  colspan=\"2\">").append(pop.getContent()).append("</td></tr>").
                  append("</table></font>").
                  append("</body></html>");
          String a = sb.toString();
          detailEditorPane.setText(a);
          detailEditorPane.setCaretPosition(0);
        } else if (namaObjectPilihan.matches("newsclusteringlucene.utils.Cluster")) {
          Cluster c = (Cluster) node.getUserObject();
          StringBuilder sb = new StringBuilder();

          sb.append("<html><head><title>").append(c.getClusterLabel()).append("</title></head><body>").
                  append("<font size=7>").append(c.getClusterLabel()).append("</font><br><br>").
                  append("<font size=3><table border =0>"). // cluster document members
                  append("<tr><td colspan=\"2\" align=\"center\"><h2>Cluster Document Members</h2></td></tr>").
                  append("<tr><td align=\"center\"><b>No.</b></td>"
                  + "<td align=\"center\"><b>Document</b></td></tr>");
          for (int i = 0; i < c.getClusterDocs().size(); i++) {
            sb.append("<tr><td align=\"right\">").append(i + 1).append("</td><td>").
                    append(c.getClusterDocs().get(i).getTitle()).append("</td></tr>");
          }
          sb.append("</table></font>").
                  append("<br><br>").
                  append("<font size=3><table border =0>"). // cluster lable candidate
                  append("<tr><td colspan=\"4\" align=\"center\"><h2>Cluster Labels Candidate (Sorted)</h2></td></tr>").
                  append("<tr><td align=\"center\"><b>No.</b></td>"
                  + "<td align=\"center\"><b>Phrase</b></td>"
                  + "<td align=\"center\"><b>#Doc in Cluster</b></td>"
                  + "<td align=\"center\"><b>#Appear in Cluster</b></td>"
                  + "<td align=\"center\"><b>Labeling Score</b></td>"
                  + "</tr>");
          for (int i = 0; i < c.getClusterLabels().length; i++) {
            String phrase = c.getClusterLabels()[i];
            sb.append("<tr><td align=\"right\">").append(i + 1).
                    append("</td><td align=\"right\">").append(phrase).
                    append("</td><td align=\"center\">").append(c.getClustStat().getClusterPhraseDocumentFrequency().get(phrase)).
                    append("</td><td align=\"center\">").append(c.getClustStat().getClusterPhraseFrequency().get(phrase)).
                    append("</td><td align=\"left\">").append(c.getClusterLabelsScore().get(phrase)).append("</td></tr>");
          }
          sb.append("</table></font>").
                  append("</body></html>");

          String a = sb.toString();
          detailEditorPane.setText(a);
          detailEditorPane.setCaretPosition(0);
        }
      }
    }//GEN-LAST:event_newsTreeValueChanged

    private void evaluationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_evaluationButtonActionPerformed
      JFrame mainFrame = NewsClusteringLuceneApp.getApplication().getMainFrame();
      EvaluationViewDialog.showDialog(mainFrame, true, 
              appConf, vertices, mergedClusters);
    }//GEN-LAST:event_evaluationButtonActionPerformed

  public static void openAction(File targetFile, Configuration appConf) {
    NewsClusteringLuceneView.appConf = appConf;
    try {
      System.out.println("STARTING");
      long totalTimeStart = System.currentTimeMillis();
      // PARSING -> COUNT TF-IDF, EXTRACT PHRASES
      System.out.print("PARSING: ");
      long start = System.currentTimeMillis();
      parser = new LuceneIndexParser(targetFile.getAbsolutePath(), appConf);
      ArrayList<LuceneIndexDocument> pop = parser.getDocuments(); // render the input to population
      long end = System.currentTimeMillis();
      System.out.println("document parsing time: " + TimingUtil.elapsedTime(start, end));

      // GENERATING GRAPH
      System.out.print("GENERATING GRAPH: ");
      start = System.currentTimeMillis();
      DistanceCalculator dcalc = new CosineSimilarityCalc();
      LuceneDistanceGraphGenerator generator = new LuceneDistanceGraphGenerator(dcalc); //new generator with calcBasic
      DistanceGraph distanceGraph = generator.generateGraph(pop); //generate graph from population
      end = System.currentTimeMillis();
      System.out.println("graph generator time: " + TimingUtil.elapsedTime(start, end));
      
      //CUSTOM - BY TOPIC MANUAL CLUSTERING

      // MERGING
      System.out.print("MERGING GRAPH: ");
      start = System.currentTimeMillis();
      ClusterMerging merging = null;
      if (appConf.clusterMergingMethod == Configuration.MERGING_NATURAL_TRESHOLD) {
        merging = new ClusterMergingUPGMANaturalTreshold(pop, generator, distanceGraph);
      } else if (appConf.clusterMergingMethod == Configuration.MERGING_SPECIFIED_TRESHOLD) {
        merging = new ClusterMergingUPGMASpecifiedTreshold(distanceGraph, NewsClusteringLuceneView.appConf.dendogramCuttingTreshold);
      } else if (appConf.clusterMergingMethod == Configuration.MERGING_SPECIFIED_NUMBER_OF_CLUSTER_TRESHOLD) {
        merging = new ClusterMergingUPGMASpecifiedClusterNumber(distanceGraph, NewsClusteringLuceneView.appConf.specifiedNumberOfCluster);
      } else if (appConf.clusterMergingMethod == Configuration.MERGING_MANUAL_BY_TOPIC) {
        merging = new ClusterMergingManualByTopic(pop, generator, distanceGraph);
      }
      distanceGraph = merging.getMergedGraph();
      end = System.currentTimeMillis();
      System.out.println("graph merger time: " + TimingUtil.elapsedTime(start, end));

      // COUNT CLUSTER AND COLLECTION STATISTICS
      System.out.print("COUNT STATISTICS: ");
      start = System.currentTimeMillis();
      ArrayList<Vertex> vertices = distanceGraph.getVertice();
      Collections.sort(vertices, new byMostArticles()); // sort cluster by Most Document a Cluster Contains
      CollectionStat collStat = new CollectionStat(vertices, appConf); // generate collection statistics and all of the clusters statistics
      end = System.currentTimeMillis();
      System.out.println("statistics calculation time: " + TimingUtil.elapsedTime(start, end));
      
      NewsClusteringLuceneView.vertices = vertices;

      DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("root node (" + collStat.getNumOfDoc() + " docs)");
      DefaultMutableTreeNode otherNode = new DefaultMutableTreeNode("other"); // for outliners node
      DefaultMutableTreeNode oldNode = new DefaultMutableTreeNode("old"); // for old node

      // CLUSTER LABELING AND PROCESS UI NAVIGATION
      System.out.print("GENERATE CLUSTER LABEL AND UI: ");
      start = System.currentTimeMillis();
      
      int otherNodeCounter = 0;
      int oldNodeCounter = 0;
      for (Vertex vertex : vertices) { // root vertex (cluster vertex)
        ArrayList<LuceneIndexDocument> clusterDocs = vertex.getClusterMembers();
       
        if (clusterDocs.size() > 1) { // only process vertex(cluster) that have merged atleast once for cluster label
          Cluster c = getCluster(appConf, vertex.getClusterStatistics(), collStat, clusterDocs);
          mergedClusters.add(c);
          DefaultMutableTreeNode cNode = new DefaultMutableTreeNode(c); // set cluster name
          for (LuceneIndexDocument doc : clusterDocs) { // vertex cluster members
            // add each document to cluster node
            cNode.add(new DefaultMutableTreeNode(doc)); // insert cluster member
          }
          rootNode.add(cNode); // add cluster to root
          
        } else if (!vertex.isIncludeInClustering()){
          oldNode.add(new DefaultMutableTreeNode(clusterDocs.get(0))); // insert to other "old" node
          oldNodeCounter += 1;
        } else {
          otherNode.add(new DefaultMutableTreeNode(clusterDocs.get(0))); // insert to other "other" node
          otherNodeCounter += 1;
        }
        otherNode.setUserObject("other (" + otherNodeCounter + ")");
        rootNode.add(otherNode); // add otherNode (outliners) into the rootNode
        oldNode.setUserObject("old (" + oldNodeCounter + ")");
        rootNode.add(oldNode); // add otherNode (outliners) into the rootNode
      }
      newsTree.setModel(new DefaultTreeModel(rootNode));
      end = System.currentTimeMillis();
      System.out.println("cluster labeling and UI generation time: " + TimingUtil.elapsedTime(start, end));

      long totalTimeEnd = System.currentTimeMillis();
      System.out.println("Total time: " + TimingUtil.elapsedTime(totalTimeStart, totalTimeEnd));

      System.out.println("FINISHED");
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(null,
              "Error Opening Lucene Index:\n" + targetFile.getAbsolutePath()
              + "\n\nError Message:\n" + ex.toString(),
              "Error Opening Lucene Index", JOptionPane.ERROR_MESSAGE);
      ex.printStackTrace();
    }
  }

  private static Cluster getCluster(Configuration appConf, ClusterStat clustStat, CollectionStat colStat, ArrayList<LuceneIndexDocument> docs) {

    // GENERATE CLUSTER LABEL
    ClusterLabeling labeling = null;    
    if (appConf.clusterLabelingMethod == Configuration.LABELING_MUTUAL_INFORMATION) {
      labeling = new ClusterLabelingMutualInformation(appConf, clustStat, colStat, appConf.clusterLabelScaling);
    } else if (appConf.clusterLabelingMethod == Configuration.LABELING_CHI_SQUARE_SELECTION) {
      labeling = new ClusterLabelingChiSquareSelection(appConf, clustStat, colStat, appConf.clusterLabelScaling);
    } else if (appConf.clusterLabelingMethod == Configuration.LABELING_RANK_D_SCORE) {
      labeling = new ClusterLabelingRankFeatureDScore(appConf, clustStat, colStat, appConf.clusterLabelScaling); 
    } else if (appConf.clusterLabelingMethod == Configuration.LABELING_MAX_TFIDF_BY_CLUSTER) {
      labeling = new ClusterLabelingMaxTFIDFByCluster(appConf, clustStat, colStat, appConf.clusterLabelScaling);
    } else if (appConf.clusterLabelingMethod == Configuration.LABELING_MAX_TFIDF_BY_COLLECTION) {
      labeling = new ClusterLabelingMaxTFIDFByCollection(appConf, clustStat, colStat, appConf.clusterLabelScaling);
    }    
    Cluster c = new Cluster(labeling.getClusterLabel(), labeling.getClusterLabels(), labeling.getClusterLabelsScore(), docs, clustStat);
    return c;
  }

  public void windowClosing(WindowEvent we) {
    parser.closeLuceneIndex();
  }

  // <editor-fold defaultstate="collapsed" desc="Interfaces not used">
  public void windowOpened(WindowEvent we) {
  }

  public void windowClosed(WindowEvent we) {
  }

  public void windowIconified(WindowEvent we) {
  }

  public void windowDeiconified(WindowEvent we) {
  }

  public void windowActivated(WindowEvent we) {
  }

  public void windowDeactivated(WindowEvent we) {
  }
  // </editor-fold>

  public static class byMostArticles implements java.util.Comparator {

    public int compare(Object t, Object t1) {
      int max = ((Vertex) t1).getClusterMembers().size() - ((Vertex) t).getClusterMembers().size();
      return max;
    }
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JSplitPane ExplorerSplitPane;
  private javax.swing.JScrollPane clusterThreeScrollPane;
  private javax.swing.JMenuItem configurationMenuItem;
  private javax.swing.JEditorPane detailEditorPane;
  private javax.swing.JScrollPane detailScrollPane;
  private javax.swing.JMenu editMenu;
  private javax.swing.JButton evaluationButton;
  private javax.swing.JPanel mainPanel;
  private javax.swing.JMenuBar menuBar;
  private static javax.swing.JTree newsTree;
  private javax.swing.JButton openAndConfigurationButton;
  private javax.swing.JProgressBar progressBar;
  private javax.swing.JLabel statusAnimationLabel;
  private javax.swing.JLabel statusMessageLabel;
  private javax.swing.JPanel statusPanel;
  // End of variables declaration//GEN-END:variables

  private JFileChooser chooser;
  private static LuceneIndexParser parser;
  
  private static ArrayList<Vertex> vertices;
  private static ArrayList<Cluster> mergedClusters = new ArrayList<Cluster>();;
  
  private static Configuration appConf = new Configuration();
  private final Timer messageTimer;
  private final Timer busyIconTimer;
  private final Icon idleIcon;
  private final Icon[] busyIcons = new Icon[15];
  private int busyIconIndex = 0;
  private JDialog aboutBox;
}
