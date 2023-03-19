#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Thu Apr  7 15:17:53 2022

@author: isaacmenis
"""

import re
from collections import Counter

def most_frequent(List):
    occurence_count = Counter(List)
    return occurence_count.most_common(1)[0][0]

def find_urls(text):
    urls = re.findall('http[s]?://(?:[a-zA-Z]|[0-9]|[$-_@.&+]|[!*\(\),]|(?:%[0-9a-fA-F][0-9a-fA-F]))+', str(text))
    return urls

def test_find_urls():
    # Test 1 - A single URL as input
    case_1_input = 'https://www.github.com/'
    case_1_output = ["https://www.github.com/"]
    assert find_urls(case_1_input) == case_1_output

    # Test 2 - longer URLs
    case_2_input = "https://www.github.com/lspitzley/bfor206_spring2022"
    case_2_output = ["https://www.github.com/lspitzley/bfor206_spring2022"]
    assert find_urls(case_2_input) == case_2_output

    # Test 3 - no URLs
    case_3_input = "There are no URLs in this string"
    case_3_output = []
    assert find_urls(case_3_input) == case_3_output

    # Test 4 - multiple URLs
    case_4_input = "I like https://github.com, I also like https://bitbucket.com/"
    case_4_output = ["https://github.com,", "https://bitbucket.com/"]
    assert find_urls(case_4_input) == case_4_output

    # Test 5 - numpy nan value as input
    import numpy as np
    case_5_input = np.nan
    case_5_output = []
    assert find_urls(case_5_input) == case_5_output
    
print(test_find_urls())