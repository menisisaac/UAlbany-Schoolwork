# -*- coding: utf-8 -*-
"""
Created on Tue May  5 09:03:27 2020

@author: lee
"""

import pandas as pd
import nltk
import irc_parse
from nltk.corpus import words
from collections import Counter
import re



raw_data = []

data = open('hackers.log', 'r', errors='ignore')

raw_data = data.readlines()

hackers = pd.DataFrame(raw_data, columns=['original_data'])

hackers['is_date_row'] = hackers['original_data'].apply(irc_parse.is_date_row)

hackers['is_message_row'] = hackers['original_data'].apply(irc_parse.is_message)

hackers['is_join_quit'] = hackers['original_data'].apply(irc_parse.is_join_quit)

hackers['join_or_quit'] = hackers.loc[hackers['is_join_quit']==True, 'original_data'].apply(irc_parse.get_join_quit_type)

hackers['chat_words'] = hackers.loc[hackers['is_message_row']==True, 'original_data'].apply(irc_parse.get_chat_message)

hackers['chat_word_list'] = hackers.loc[hackers['is_message_row']==True, 'chat_words'].apply(lambda x: nltk.tokenize.word_tokenize(x.lower()))

hackers['time'] = hackers.loc[hackers['is_date_row']!=True, 'original_data'].apply(irc_parse.get_hours_minutes)

hackers['username'] = hackers.loc[hackers['is_message_row']==True, 'original_data'].apply(irc_parse.get_user_name)
hackers['leave_join_usernames'] = hackers.loc[hackers['is_join_quit']==True, 'original_data'].apply(irc_parse.get_join_quit_username)
hackers['prefix'] = hackers.loc[(hackers['is_message_row']==True) | (hackers['is_join_quit']==True), 'original_data'].apply(irc_parse.get_user_prefix)
hackers['url'] = hackers['original_data'].apply(irc_parse.find_urls)
full_url_list = hackers.loc[hackers['url']!=None, 'url'].apply(irc_parse.find_urls)
hackers['has_text'] = hackers.loc[hackers['is_message_row']==True, 'original_data'].apply(irc_parse.has_words)

date_row_index = hackers[hackers['is_date_row']==True].index

hackers['date'] = ''
for x in range(len(date_row_index)):
    if x+1 != len(date_row_index):
        mini = date_row_index[x]
        maxi = date_row_index[x+1]
        hackers.loc[hackers.index[mini:maxi], 'date'] = irc_parse.get_current_date(hackers.at[mini, 'original_data'])
    else:
        mini = date_row_index[x]
        maxi = len(hackers.index)
        hackers.loc[hackers.index[mini:maxi], 'date'] = irc_parse.get_current_date(hackers.at[mini, 'original_data'])
hackers['date'] = pd.to_datetime(hackers['date'])
hackers['datetime'] = hackers.loc[hackers['is_message_row']==True].apply(lambda x : irc_parse.get_datetime(x['date'], x['time']), axis=1)

hackers.to_csv('hackers_clean.csv')











