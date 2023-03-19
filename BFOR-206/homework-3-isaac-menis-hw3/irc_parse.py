"""
Module containing functions to parse IRC logs.
"""

import re
import datetime
from collections import Counter


def sanity_check():
    """This test should always pass.
    The purpose is to make sure Github actions are functioning properly.
    """
    return True


def get_chat_message(row):
    message = re.split(r'> ', row)

    return "> ".join(message[1:])


def get_current_date(dateline):
    """    Parse the IRC log date format to find the current date

    Return a POSIX (datetime) form date for midnight

    Args:
        dateline (str): Row that begins with `---`

    Returns:
        datetime: datetime object with the date from the row 
    """
    ids = re.findall(r'(?:^|(?<=\s))\w{3}(?=\s|$)', dateline)
    months = {'Jan': 1, 'Feb': 2, 'Mar': 3, 'Apr': 4, 'May': 5, 'Jun': 6,
              'Jul': 7, 'Aug': 8, 'Sep': 9, 'Oct': 10, 'Nov': 11, 'Dec': 12}
    for key in months:
        if key in ids:
            month = months[key]
    year = int(re.search(r'[0-9]{4}', dateline).group())
    date = int(re.search(r' [0-9][0-9] ', dateline).group())
    date_final = datetime.datetime(year, month, date, 0, 0)
    return  date_final


def get_hours_minutes(time_row):
    """ covered in class 11-2 """
    results = re.search(
        r'(?<![:-[0-9])[0-9]{2}:[0-9]{2}(?![:-[0-9])', time_row)
    if results == None:
        return {}
    hours = results.group(0).split(':')[0]
    if hours[0] == '0':
        hours = hours[1]
    minutes = results.group(0).split(':')[1]
    if minutes[0] == '0':
        minutes = minutes[1]
    hours = int(hours)
    minutes = int(minutes)
    return {'hour': hours, 'minute': minutes}


def get_join_quit_type(row):
    """Returns if a row is a join or a quit,


    Args:
        row (str): join or quit row

    Returns:
        str: "join" or "quit"
    """
    if re.search(r'((?=.*-!-)(?=.*quit|.*left))', row):
        return 'quit'
    else:
        return 'join'


def get_join_quit_username(row):
    """ Input a join/quit row, get a username back.

    Args:
        row (str): Must be a join/quit row. These
        have `-!-` after the timestamp.

    Returns:
        str: the username from the row.
    """
    first = row.split(' ')[2]
    first = first.strip()
    return first


def get_user_name(row):
    """
    Find the username in a chat row.

    Parameters
    ----------
    row : str
        row that contains a message.

    Returns
    -------
    string
        string with username.

    """
    special_char = '!@#$%&?~+_-'
    first = row.split('>')[0]
    second = first.split('<')[1]
    second = second.strip()
    for x in special_char:
        if len(second) > 0:
            if second[0] == x:
                second = second[1:]
    return second


def get_user_prefix(row):
    """ Gets the prefix of a username, if any.
    If there is not a prefix, return None.

    For example, '@' or '+'.

    Args:
        row (str): chat message row.

    Returns:
        str: the user prefix (if any)
    """
    special_char = '!@#$%&?~+_-'
    if is_message(row):
        first = row.split('>')[0]
        second = first.split('<')[1]
        username = second.strip()
    elif is_join_quit(row):
        username = get_join_quit_username(row)
    for x in range(len(username)):
        if username[0].isalpha():
            return None
        else:
            for y in special_char:
                if y == username[x]:
                    return y


def is_date_row(row):
    """
    Check if row indicates date change.
    Row contains --- at the start

    Parameters
    ----------
    row : TYPE
        DESCRIPTION.

    Returns
    -------
    bool
        DESCRIPTION.

    """
    if re.search(r'---', row) and not is_message(row) and not is_join_quit(row):
        return True
    return False


def is_join_quit(row):
    """
    Check if message is a join/quit/metadata row.
    Row contains -!- at the start

    Parameters
    ----------
    row : str
        row that you want to check.


    Returns
    -------
    bool
        DESCRIPTION.

    """
    if re.search(r'((?=.*-!-)(?=.*quit|.*joined|.*left))', row):
        return True
    else:
        return False


def is_message(row):
    """
    Determine if a row contains a message.

    Parameters
    ----------
    row : str
        row from chat log.

    Returns
    -------
    bool
        True if row belongs to chat log.

    """
    if row[6] == '<':
        return True
    else:
        return False
    
def has_words(row):
    words = get_chat_message(row)
    for x in words:
        if x.isalpha():
            return True
        else:
            return False


def find_urls(text):
    urls = re.findall(
        'http[s]?://(?:[a-zA-Z]|[0-9]|[$-_@.&+]|[!*\(\),]|(?:%[0-9a-fA-F][0-9a-fA-F]))+', str(text))
    return urls



def get_admin_flag(row):
    if is_message(row):
        return (row.split('<')[1])[0]


def is_emote(row):
    row = re.sub(' +', ' ', row)
    if row.split(' ')[1] == '*':
        return True
    else:
        return False


def is_topic(row):
    if re.search(r'(?=.*-!-)', row) or re.search(r'Channel Topic: ', row):
        if 'changed the topic' in row or re.search(r'Channel Topic', row):
            return True
        else:
            return False
def get_datetime(date, time):
    date = date.strftime('%Y-%m-%d %H:%M:%S')
    date = datetime.datetime.strptime(date, '%Y-%m-%d %H:%M:%S')
    date_and_time = date + datetime.timedelta(hours=time['hour'],minutes=time['minute'])
    return date_and_time

def most_frequent(List):
    occurence_count = Counter(List)
    return occurence_count.most_common(1)[0][0]
