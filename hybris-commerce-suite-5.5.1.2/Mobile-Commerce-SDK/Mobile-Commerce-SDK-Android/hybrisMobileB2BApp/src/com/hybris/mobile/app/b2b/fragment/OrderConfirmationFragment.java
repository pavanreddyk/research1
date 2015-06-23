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
package com.hybris.mobile.app.b2b.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.hybris.mobile.app.b2b.B2BApplication;
import com.hybris.mobile.app.b2b.IntentConstants;
import com.hybris.mobile.app.b2b.R;
import com.hybris.mobile.app.b2b.activity.CatalogActivity;
import com.hybris.mobile.app.b2b.adapter.OrderProductListAdapter;
import com.hybris.mobile.app.b2b.helper.OrderHelper;
import com.hybris.mobile.app.b2b.utils.UIUtils;
import com.hybris.mobile.lib.b2b.data.DataError;
import com.hybris.mobile.lib.b2b.data.order.Order;
import com.hybris.mobile.lib.b2b.data.order.OrderProduct;
import com.hybris.mobile.lib.b2b.query.QueryOrderDetails;
import com.hybris.mobile.lib.b2b.response.ResponseReceiver;
import com.hybris.mobile.lib.http.listener.OnRequestListener;
import com.hybris.mobile.lib.http.response.Response;
import com.hybris.mobile.lib.http.utils.RequestUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;


/**
 * Container that handle_anchor the details information for a specific order
 */
public class OrderConfirmationFragment extends Fragment implements ResponseReceiver<Order> {
    private String mOrderRequestId = RequestUtils.generateUniqueRequestId();
    private TextView mOrderConfirmNumber;
    private TextView mOrderConfirmEmail;

    private OrderProductListAdapter mOrderProductListAdapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_confirmation, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mOrderConfirmNumber = (TextView) getView().findViewById(R.id.order_confirm_number_text);
        mOrderConfirmEmail = (TextView) getView().findViewById(R.id.order_confirm_email);


        // Product list
        ListView productList = (ListView) getActivity().findViewById(R.id.order_products_list);
        mOrderProductListAdapter = new OrderProductListAdapter(getActivity(), new ArrayList<OrderProduct>());
        productList.setAdapter(mOrderProductListAdapter);

        getView().findViewById(R.id.order_confirm_continue_shopping_button).setOnClickListener(
                orderConfirmContinueShoppingButtonListener);
        QueryOrderDetails mQueryOrderDetails = new QueryOrderDetails();
        mQueryOrderDetails.setCode(getActivity().getIntent().getStringExtra(IntentConstants.ORDER_CODE));

        // Getting the order
        B2BApplication.getContentServiceHelper().getOrder(this, mOrderRequestId, mQueryOrderDetails, false, null,
                new OnRequestListener() {

                    @Override
                    public void beforeRequest() {
                        UIUtils.showLoadingActionBar(getActivity(), true);
                        getView().setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void afterRequest(boolean isDataSynced) {
                        getView().setVisibility(View.VISIBLE);
                        UIUtils.showLoadingActionBar(getActivity(), false);
                    }
                });
    }

    @Override
    public void onResponse(Response<Order> response) {
        populateOrder(response.getData());
    }

    @Override
    public void onError(Response<DataError> response) {
        UIUtils.showError(response, getActivity());
    }

    private void populateOrder(Order order) {
        if (order != null) {

            if (StringUtils.isNotBlank(order.getCode())) {

                mOrderConfirmNumber.setText(getString(R.string.order_confirmation_number, order.getCode()));
            }

            // Display user email and billing address
            if (order.getUser() != null && StringUtils.isNotBlank(order.getUser().getUid())) {
                mOrderConfirmEmail.setText(getString(R.string.order_confirmation_detail, order.getUser().getUid()));

            }

            // fill Delivery info
            OrderHelper.createDeliverySummary(getActivity(), order);

            // fill order summary
            OrderHelper.createOrderSummary(getActivity(), order);

            mOrderProductListAdapter.clear();
            mOrderProductListAdapter.addAll(order.getDeliveryOrderGroups().get(0).getEntries());

            // Updating the list
            mOrderProductListAdapter.notifyDataSetChanged();
        }
    }


    /**
     * Continue shopping : browse to the catalog
     */
    public OnClickListener orderConfirmContinueShoppingButtonListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            startActivity(new Intent(getActivity(), CatalogActivity.class));
        }

    };


    @Override
    public void onStop() {
        super.onStop();
        B2BApplication.getContentServiceHelper().cancel(mOrderRequestId);
    }
}
