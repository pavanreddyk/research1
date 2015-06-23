/*******************************************************************************
 * [y] hybris Platform
 *  
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 ******************************************************************************/
package com.hybris.mobile.app.b2b.adapter;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hybris.mobile.app.b2b.B2BApplication;
import com.hybris.mobile.app.b2b.R;
import com.hybris.mobile.app.b2b.helper.ProductHelper;
import com.hybris.mobile.app.b2b.utils.UIUtils;
import com.hybris.mobile.lib.b2b.data.product.Product;
import com.hybris.mobile.lib.b2b.service.ContentServiceHelper;
import com.hybris.mobile.lib.http.listener.OnRequestListener;


/**
 * Adapter for the products of the catalog in a gridview
 */
public class ProductGridAdapter extends ProductItemsAdapter
{
	public static final String TAG = ProductGridAdapter.class.getCanonicalName();

	/**
	 * Contains all UI elements for Product to improve gridview display while scrolling
	 */
	static class ViewHolder
	{
		TextView productName;
		TextView productNo;
		TextView productPrice;
		ImageView productImageViewCartIcon;
		ImageView productImageViewExpandIcon;
		ImageView productMainImage;
		ProgressBar productImageLoading;
	}

	public ProductGridAdapter(Context context, Cursor c, int flags, ContentServiceHelper contentServiceHelper)
	{
		super(context, c, flags, contentServiceHelper);
	}

	@Override
	public void bindView(View rowView, final Context context, final Cursor cursor)
	{

		// When clicking outside a EditText, hide keyboard, remove focus and reset to the default value
		// Clicking on the main view 
		rowView.setOnTouchListener(new OnTouchListener()
		{

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				UIUtils.hideKeyboard(getContext());
				v.performClick();
				return false;
			}
		});


		final Product product = getData();
		final ViewHolder productGridViewHolder = (ViewHolder) rowView.getTag();

		// Loading the product image
		if (B2BApplication.isOnline())
		{
			if (StringUtils.isNotBlank(product.getImageThumbnailUrl()))
			{
				B2BApplication.getContentServiceHelper().loadImage(product.getImageThumbnailUrl(),
						"product_grid_image_" + product.getCode(), productGridViewHolder.productMainImage, 0, 0, true,
						new OnRequestListener()
						{

							@Override
							public void beforeRequest()
							{
								productGridViewHolder.productMainImage.setImageResource(android.R.color.transparent);
								productGridViewHolder.productMainImage.setVisibility(View.GONE);
								productGridViewHolder.productImageLoading.setVisibility(View.VISIBLE);
							}

							@Override
							public void afterRequest(boolean isDataSynced)
							{
								productGridViewHolder.productMainImage.setVisibility(View.VISIBLE);
							}
						}, true);
			}

		}
		else
		{
			Log.i(TAG, "Application offline, displaying no image for product " + product.getCode());
			productGridViewHolder.productMainImage.setImageDrawable(getContext().getResources().getDrawable(
					R.drawable.no_image_product));
			productGridViewHolder.productMainImage.setVisibility(View.VISIBLE);
		}


		// Populate name and code for a product when row collapsed
		productGridViewHolder.productName.setText(product.getName());
		productGridViewHolder.productNo.setText(product.getCode());
		productGridViewHolder.productPrice.setText(product.getPriceRangeFormattedValue());


		if (product.isMultidimensional())
		{
			//Show arrow down with variants
			productGridViewHolder.productImageViewCartIcon.setVisibility(View.GONE);
			productGridViewHolder.productImageViewExpandIcon.setVisibility(View.VISIBLE);
			productGridViewHolder.productImageViewExpandIcon.setEnabled(true);
			productGridViewHolder.productImageViewExpandIcon.setClickable(true);
		}
		else
		{
			productGridViewHolder.productImageViewCartIcon.setVisibility(View.VISIBLE);
			productGridViewHolder.productImageViewExpandIcon.setVisibility(View.GONE);
			productGridViewHolder.productImageViewCartIcon.setEnabled(false);
			productGridViewHolder.productImageViewCartIcon.setClickable(false);
		}


		/**
		 * Product item row is expanded and user click on the main part of the cell to navigate to the product detail page
		 */
		productGridViewHolder.productMainImage.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ProductHelper.redirectToProductDetail(getContext(),
						StringUtils.isNotBlank(product.getFirstVariantCode()) ? product.getFirstVariantCode() : product.getCode());
			}
		});


		/**
		 * Product item row is expanded and user click on the product name of the cell to navigate to the product detail
		 * page
		 */
		productGridViewHolder.productName.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ProductHelper.redirectToProductDetail(getContext(),
						StringUtils.isNotBlank(product.getFirstVariantCode()) ? product.getFirstVariantCode() : product.getCode());
			}
		});

	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2)
	{
		View rowView = mInflater.inflate(R.layout.item_product_gridview, arg2, false);

		ViewHolder viewHolder = new ViewHolder();
		viewHolder.productName = (TextView) rowView.findViewById(R.id.product_item_name);
		viewHolder.productNo = (TextView) rowView.findViewById(R.id.product_item_no);
		viewHolder.productPrice = (TextView) rowView.findViewById(R.id.product_item_price);
		viewHolder.productImageViewCartIcon = (ImageView) rowView.findViewById(R.id.product_item_button_cart_icon);
		viewHolder.productImageViewExpandIcon = (ImageView) rowView.findViewById(R.id.product_item_button_expand_icon);
		viewHolder.productMainImage = (ImageView) rowView.findViewById(R.id.product_item_image);
		viewHolder.productImageLoading = (ProgressBar) rowView.findViewById(R.id.product_item_image_loading);

		rowView.setTag(viewHolder);

		return rowView;
	}
}
