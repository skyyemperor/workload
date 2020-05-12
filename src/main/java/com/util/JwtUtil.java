package com.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    /**
     * 过期时间一月
     */
    private static final long EXPIRE_TIME = 30 * 24 * 60 * 60 * 1000L;
    /**
     * token私钥
     */
    private static final String TOKEN_SECRET = "f25ki87c28064d0e855e72c0a6a0e618";

    /**
     * 校验token是否正确
     *
     * @param token 密钥
     * @return 是否正确
     */
    public static boolean verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }


    /**
     * 获取学号
     *
     * @param token
     * @return
     */
    public static String getUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userId").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 判断当前日期是否在token失效日期之前的七天内
     * @param token
     * @return
     */
    public static boolean judgeExpireDate(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            //获取token失效日期
            LocalDate endDate = DateUtil.date2LocalDate(jwt.getExpiresAt());
            //当前日期在token失效日期之前的七天内
            if (endDate.minusDays(7).isBefore(LocalDate.now())) {
                return true;
            }
        } catch (JWTDecodeException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新token，并返回新的token
     * @param token
     * @return
     */
    public static String refreshToken(String token) {
        String userId = getUserId(token);
        return sign(userId);
    }

    /**
     * 生成签名,一个月后过期
     *
     * @param userId 用户名
     * @return 加密的token
     */
    public static String sign(String userId) {
        try {
            //过期时间
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            //私钥及加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            //设置头部信息
            Map<String, Object> header = new HashMap<>(2);
            header.put("typ", "JWT");
            header.put("alg", "HS256");
            // 附带userId信息，生成签名
            return JWT.create()
                    .withHeader(header)
                    .withClaim("userId", userId)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            return null;
        }
    }
}