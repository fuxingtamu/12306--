package com.railway.controller;

import com.railway.common.Result;
import com.railway.config.UserPrincipal;
import com.railway.dto.BuyTicketDTO;
import com.railway.service.OrderService;
import com.railway.vo.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单控制器
 */
@Tag(name = "订单接口", description = "购票、订单管理相关接口")
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 购票下单
     */
    @Operation(summary = "购票下单")
    @PostMapping("/buy")
    public Result<OrderVO> buyTicket(@Valid @RequestBody BuyTicketDTO buyTicketDTO) {
        try {
            Long userId = getCurrentUserId();
            OrderVO order = orderService.buyTicket(userId, buyTicketDTO);
            return Result.success("下单成功，请在30分钟内完成支付", order);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 支付订单
     */
    @Operation(summary = "支付订单")
    @PostMapping("/{id}/pay")
    public Result<Void> payOrder(@PathVariable Long id, @RequestParam Integer payType) {
        try {
            Long userId = getCurrentUserId();
            orderService.payOrder(userId, id, payType);
            return Result.success("支付成功", null);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 取消订单
     */
    @Operation(summary = "取消订单")
    @PostMapping("/{id}/cancel")
    public Result<Void> cancelOrder(@PathVariable Long id) {
        try {
            Long userId = getCurrentUserId();
            orderService.cancelOrder(userId, id);
            return Result.success("订单已取消", null);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 获取订单列表
     */
    @Operation(summary = "获取订单列表")
    @GetMapping
    public Result<List<OrderVO>> getOrderList(@RequestParam(required = false) Integer status) {
        try {
            Long userId = getCurrentUserId();
            List<OrderVO> orders = orderService.getOrderList(userId, status);
            return Result.success(orders);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 获取订单详情
     */
    @Operation(summary = "获取订单详情")
    @GetMapping("/{id}")
    public Result<OrderVO> getOrderDetail(@PathVariable Long id) {
        try {
            Long userId = getCurrentUserId();
            OrderVO order = orderService.getOrderDetail(userId, id);
            return Result.success(order);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 退票
     */
    @Operation(summary = "退票")
    @PostMapping("/{orderId}/refund/{itemId}")
    public Result<Void> refundTicket(@PathVariable Long orderId, @PathVariable Long itemId) {
        try {
            Long userId = getCurrentUserId();
            orderService.refundTicket(userId, orderId, itemId);
            return Result.success("退票成功", null);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 改签
     */
    @Operation(summary = "改签")
    @PostMapping("/{orderId}/change")
    public Result<OrderVO> changeTicket(@PathVariable Long orderId, @Valid @RequestBody BuyTicketDTO changeDTO) {
        try {
            Long userId = getCurrentUserId();
            OrderVO newOrder = orderService.changeTicket(userId, orderId, changeDTO);
            return Result.success("改签成功", newOrder);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 获取当前登录用户ID
     */
    private Long getCurrentUserId() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getUserId();
    }
}