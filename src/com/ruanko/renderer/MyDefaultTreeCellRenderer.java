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
	    //ִ�и���ԭ�Ͳ���  
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,  
                row, hasFocus);  
		 //�õ�ÿ���ڵ��TreeNode  
        DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) value;  
          
        //�õ�ÿ���ڵ��text  
        String str = treeNode.toString();         
          
        //�ж����ĸ��ı��Ľڵ����ö�Ӧ��ֵ����������ڵ㴫�����һ��ʵ��,����Ը���ʵ�������һ��������������ʾ��Ӧ��ͼ�꣩  
        if (str .equals("����"))  
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

