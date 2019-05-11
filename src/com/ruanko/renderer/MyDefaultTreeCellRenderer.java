package com.ruanko.renderer;

import java.awt.Component;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class MyDefaultTreeCellRenderer extends DefaultTreeCellRenderer{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
	    //执行父类原型操作  
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,  
                row, hasFocus);  
		 //得到每个节点的TreeNode  
        DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) value;  
          
        //得到每个节点的text  
        String str = treeNode.toString();         
          
        //判断是哪个文本的节点设置对应的值（这里如果节点传入的是一个实体,则可以根据实体里面的一个类型属性来显示对应的图标）  
        if (str .equals("新闻"))  
        {  
        	ImageIcon icon = new ImageIcon("images/news.jpg");
			icon.setImage(icon.getImage().getScaledInstance(20,20,
					Image.SCALE_DEFAULT));
            setIcon(icon);  
        }  else{
        	ImageIcon icon = new ImageIcon("images/rss.jpg");
			icon.setImage(icon.getImage().getScaledInstance(15,15,
					Image.SCALE_DEFAULT));
            setIcon(icon);  
        }
		return this;
	}
}

