//
//  HYBProductDetailsController.h
// [y] hybris Platform
//
// Copyright (c) 2000-2015 hybris AG
// All rights reserved.
//
// This software is the confidential and proprietary information of hybris
// ("Confidential Information"). You shall not disclose such Confidential
// Information and shall use it only in accordance with the terms of the
// license agreement you entered into with hybris.

#import "HYBViewController.h"
#import "HYBViewController.h"
#import "UIViewController+Navigation.h"

@interface HYBProductDetailsController : HYBViewController <UIScrollViewDelegate>

@property (strong) NSString   *code;
@property (strong) HYBProduct *product;

- (instancetype)initWithBackEndService:(id <HYBBackEndFacade>)b2bService productId:(NSString *)selectedProductId;

@end
