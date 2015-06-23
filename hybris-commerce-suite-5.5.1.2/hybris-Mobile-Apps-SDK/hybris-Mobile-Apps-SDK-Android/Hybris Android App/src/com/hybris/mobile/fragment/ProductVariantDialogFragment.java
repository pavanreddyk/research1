/*******************************************************************************
 * [y] hybris Platform
 *  
 *   Copyright (c) 2000-2013 hybris AG
 *   All rights reserved.
 *  
 *   This software is the confidential and proprietary information of hybris
 *   ("Confidential Information"). You shall not disclose such Confidential
 *   Information and shall use it only in accordance with the terms of the
 *   license agreement you entered into with hybris.
 ******************************************************************************/
package com.hybris.mobile.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.hybris.mobile.R;


public class ProductVariantDialogFragment extends DialogFragment
{
	public static interface DoneClickListener
	{
		public void onClick(String qualifier, int position);
	}

	private String qualifier;
	private String name;
	private int selected;
	private String[] options;
	private DoneClickListener listener;

	public ProductVariantDialogFragment(String qualifier, String name, int selected, String[] options, DoneClickListener listener)
	{
		this.qualifier = qualifier;
		this.name = name;
		this.selected = selected;
		this.options = options;
		this.listener = listener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(name).setSingleChoiceItems(options, selected, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				selected = which;
			}
		}).setPositiveButton(getString(R.string.settings_done), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				listener.onClick(qualifier, selected);
			}
		});
		return builder.create();
	}

}