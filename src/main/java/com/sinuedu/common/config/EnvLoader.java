package com.sinuedu.common.config;

import org.springframework.context.annotation.Configuration;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class EnvLoader {
	static {
		Dotenv dotenv = Dotenv.configure()
							  .ignoreIfMissing() //.env 파일 없으면 오류
							  .load();
		
		System.setProperty("CLOUDFLARE_R2_BUCKET_NAME", dotenv.get("CLOUDFLARE_R2_BUCKET_NAME"));
		System.setProperty("CLOUDFLARE_R2_PUBLIC_URL", dotenv.get("CLOUDFLARE_R2_PUBLIC_URL"));
		System.setProperty("CLOUDFLARE_R2_ACCESS_KEY", dotenv.get("CLOUDFLARE_R2_ACCESS_KEY"));
		System.setProperty("CLOUDFLARE_R2_SECRET_KEY", dotenv.get("CLOUDFLARE_R2_SECRET_KEY"));
		System.setProperty("CLOUDFLARE_R2_ENDPOINT", dotenv.get("CLOUDFLARE_R2_ENDPOINT"));
		
		System.out.println("CLOUDFLARE_R2_ACCESS_KEY :" + dotenv.get("CLOUDFLARE_R2_ACCESS_KEY"));
		System.out.println("CLOUDFLARE_R2_SECRET_KEY :" + dotenv.get("CLOUDFLARE_R2_SECRET_KEY"));
	}
}
