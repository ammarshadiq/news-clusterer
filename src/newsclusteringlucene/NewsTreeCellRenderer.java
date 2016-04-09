/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newsclusteringlucene;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author shadiq
 */
public class NewsTreeCellRenderer extends DefaultTreeCellRenderer {
  
  @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        setIcon(getCustomIcon(value));
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        setIcon(getCustomIcon(value));

        return this;
    }
    private Icon getCustomIcon(Object value) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        String namaObjectPilihan = node.getUserObject().getClass().getName();
        
        // if it's a document node
        if (namaObjectPilihan.matches("newsclusteringlucene.population.LuceneIndexDocument")) {
            newsclusteringlucene.population.LuceneIndexDocument Entry = (newsclusteringlucene.population.LuceneIndexDocument) node.getUserObject();
            return ((ImageIcon) Entry.getIcon());
        } else {
          Icon icon = null;
          
          if(node.toString().startsWith("root node")){
            icon = org.jdesktop.application.Application.getInstance(newsclusteringlucene.NewsClusteringLuceneApp.class).
                          getContext().
                          getResourceMap(newsclusteringlucene.NewsClusteringLuceneView.class).
                          getIcon("NewsFavIcon.root");
          } else if (node.toString().startsWith("other")){
            icon = org.jdesktop.application.Application.getInstance(newsclusteringlucene.NewsClusteringLuceneApp.class).
                          getContext().
                          getResourceMap(newsclusteringlucene.NewsClusteringLuceneView.class).
                          getIcon("NewsFavIcon.cluster.others");
          } else if (node.toString().startsWith("old")){
            icon = org.jdesktop.application.Application.getInstance(newsclusteringlucene.NewsClusteringLuceneApp.class).
                          getContext().
                          getResourceMap(newsclusteringlucene.NewsClusteringLuceneView.class).
                          getIcon("NewsFavIcon.cluster.old");
          } else {
            icon = org.jdesktop.application.Application.getInstance(newsclusteringlucene.NewsClusteringLuceneApp.class).
                          getContext().
                          getResourceMap(newsclusteringlucene.NewsClusteringLuceneView.class).
                          getIcon("NewsFavIcon.cluster");
          }
          
          return icon;
        }
    }
  
}
