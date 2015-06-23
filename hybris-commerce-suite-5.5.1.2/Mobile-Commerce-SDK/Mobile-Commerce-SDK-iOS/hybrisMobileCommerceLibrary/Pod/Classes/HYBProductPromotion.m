//
//  HYBProductPromotion.m
// [y] hybris Platform
//
// Copyright (c) 2000-2015 hybris AG
// All rights reserved.
//
// This software is the confidential and proprietary information of hybris
// ("Confidential Information"). You shall not disclose such Confidential
// Information and shall use it only in accordance with the terms of the
// license agreement you entered into with hybris.
//


#import "HYBProductPromotion.h"
#import "HYBConsumedEntry.h"

#import "NSValueTransformer+MTLPredefinedTransformerAdditions.h"

@implementation HYBProductPromotion


+ (instancetype)productPromotionWithParams:(NSDictionary *)params {
    
    NSError *error = nil;
    
    HYBProductPromotion *productPromotion = [MTLJSONAdapter modelOfClass:[HYBProductPromotion class]
                                                                fromJSONDictionary:params
                                                                             error:&error];
    
    if (error) {
        NSLog(@"Couldn't convert JSON to HYBOrderPromotion models: %@", error);
        return nil;
    }
    
    return productPromotion;
}

+ (NSDictionary *)JSONKeyPathsByPropertyKey {
    return @{
             @"consumedEntries"         : @"consumedEntries",
             @"promotionDescription"    : @"description",
             @"promotion"               : @"promotion"
             };
}

//array
+ (NSValueTransformer *)consumedEntriesJSONTransformer {
    return [NSValueTransformer mtl_JSONArrayTransformerWithModelClass:[HYBConsumedEntry class]];
}

//dict
+ (NSValueTransformer *)promotionJSONTransformer {
    return [NSValueTransformer mtl_JSONDictionaryTransformerWithModelClass:[HYBPromotion class]];
}

@end