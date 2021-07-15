package com.giron.android.util

import com.giron.android.BuildConfig
import com.giron.android.model.net.HttpConstants

const val URL_BASE_LOCAL = "http://192.168.86.173:8000/"
const val EULA_VERSION = "20191220"
const val EULA_URL = "https://sites.google.com/giron.biz/rule/terms_of_service"

const val TAG_PATTERN = "(?=#[^\\s]*[^0-9\\s]+)#[^\\s]+"
const val TAG_PATTERN_SINGLE = "(?=[^\\s]*[^0-9\\s]+)[^\\s]+"
const val GIRON_ID_PATTERN = "#[0-9]+"
const val COMMENT_NUM_PATTERN = ">>[0-9]+"
const val GIRON_AND_COMMENT_PATTERN = "#[0-9]+>>[0-9]+"

const val MAX_TAG_COUNT = 10
const val SHARE_TWITTER = "https://twitter.com/intent/tweet?text="
