/*
 * Copyright (C) 2019 by onlymash <im@fiepi.me>, All rights reserved
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package onlymash.flexbooru.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import androidx.core.text.parseAsHtml
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.toolbar.*
import onlymash.flexbooru.BuildConfig
import onlymash.flexbooru.R
import onlymash.flexbooru.util.launchUrl

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        toolbar.title = getString(R.string.title_about_with_version, BuildConfig.VERSION_NAME)
        about.apply {
            text = SpannableStringBuilder(resources.openRawResource(R.raw.about).bufferedReader().readText()
                .parseAsHtml(HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM)).apply {
                for (span in getSpans(0, length, URLSpan::class.java)) {
                    setSpan(object : ClickableSpan() {
                        override fun onClick(view: View) {
                            if (span.url.startsWith("mailto:")) {
                                startActivity(Intent.createChooser(Intent().apply {
                                    action = Intent.ACTION_SENDTO
                                    data = span.url.toUri()
                                }, getString(R.string.share_via)))
                            } else this@AboutActivity.launchUrl(span.url)
                        }
                    }, getSpanStart(span), getSpanEnd(span), getSpanFlags(span))
                    removeSpan(span)
                }
            }
            movementMethod = LinkMovementMethod.getInstance()
        }
    }
}