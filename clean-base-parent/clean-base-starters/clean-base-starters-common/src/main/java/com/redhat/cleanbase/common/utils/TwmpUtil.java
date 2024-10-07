package com.redhat.cleanbase.common.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TwmpUtil
{
	public String getWalletCheckNumber(String walletId)
	{
		if (walletId == null)
		{
			return null;
		}
		char[] chs = walletId.toCharArray();
		int count = 0;

		for (int i = 0; i < chs.length; i++)
		{
			int num = 0;
			if (i % 2 == 1)
			{
				num += chs[i] * 2;
			} else
			{
				num += chs[i];
			}
			count += num % 10 + num / 10;
		}
		int ans = (count * 9) % 10;

		return String.valueOf(ans);
	}
}
