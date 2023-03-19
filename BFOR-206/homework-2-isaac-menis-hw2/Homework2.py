#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon Mar 28 21:20:56 2022

@author: kali
"""

import numpy as np
import pandas as pd
import text_processing


aiml_data = pd.read_csv('reddit_database.csv')

aiml_data['author_created_date'] = pd.to_datetime(aiml_data['author_created_utc'], unit = 's')
aiml_data['created_date'] = pd.to_datetime(aiml_data['created_date'])


print('Q1.1')
print(aiml_data['subreddit'].value_counts().head(5))

print('Q1.2')
print(aiml_data['author'].value_counts().head(5))

print('1.3')
print(aiml_data.groupby('subreddit')['author'].nunique().sort_values(ascending=False).head(5))

print('1.4')
print((aiml_data.groupby('subreddit')['post'].apply(lambda x : x.notnull().mean()* 100).sort_values(ascending=False).head()))

print('2.1')
aiml_data['date'] = aiml_data['created_date'].dt.date
aiml_data['date'].value_counts().plot().set(xlabel='Time', ylabel='Number of Posts', title='AI/ML Posts Per day of the Week')

print('2.2')
aiml_data['score'].hist(range=[0, 50]).set(xlabel='Points', ylabel='Percentage of Posts in Point Range', title='AI/ML Points Distribution')

print('2.3')
aiml_data['dow'] = aiml_data['created_date'].dt.day_name()
aiml_data['dow'] = pd.Categorical(aiml_data['dow'], categories= ['Monday','Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'],  ordered=True)
dow_plot = aiml_data.groupby('dow')['created_date'].count().plot(kind='bar')
dow_plot.set(xlabel='Day Of The Week', ylabel='Total Number of Posts', title='AI/ML Posts Per day of the Week')

print('2.4')
aiml_data['hod'] = aiml_data['created_date'].dt.hour
hod_plot = aiml_data.groupby('hod')['created_date'].count().plot(kind='bar')
hod_plot.set(xlabel='Hour of Day', ylabel='Number of Posts', title='AI/ML Posts Per Hour of Day')

print('3.1')
x = aiml_data.created_date.max().date()
thirty = aiml_data[aiml_data['created_date'] >= (pd.to_datetime(x) - pd.Timedelta(days=30))]
print(thirty['subreddit'].value_counts().head(5))

print('3.2')
aiml_data['title_length'] = aiml_data['title'].apply(len)
correlation = aiml_data['title_length'].corr(aiml_data['score'])
if correlation < 0.7:
    print('There is weak to no correlation')
else:
    print('There is a strong correlation')

print('3.3')
print(aiml_data['title'].str.lower().str.split(expand=True).stack().value_counts().head(20))

print('3.4')
urls_list = aiml_data['post'].apply(text_processing.find_urls)
updated_list = []
for index, value in urls_list.items():
    if value != []:
        updated_list.append(value[0])
for i in range(10):
    mode = text_processing.most_frequent(updated_list)
    print(mode)
    for k in updated_list:
        if k == mode:
            updated_list.remove(k)    











    