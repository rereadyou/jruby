/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jruby.compiler.ir.instructions;

import org.jruby.RubyRegexp;
import org.jruby.compiler.ir.Operation;
import org.jruby.compiler.ir.operands.Label;
import org.jruby.compiler.ir.operands.Operand;
import org.jruby.compiler.ir.operands.Variable;
import org.jruby.compiler.ir.representations.InlinerInfo;
import org.jruby.runtime.Block;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;

/**
 *
 * @author enebo
 */
public class Match2Instr extends Instr implements ResultInstr {
    private final Variable result;
    private final Operand receiver;
    private final Operand arg;
    
    public Match2Instr(Variable result, Operand receiver, Operand arg) {
        super(Operation.MATCH2);
        
        assert result != null: "Match2Instr result is null";
        
        this.result = result;
        this.receiver = receiver;
        this.arg = arg;
    }

    @Override
    public Operand[] getOperands() {
        return new Operand[] { receiver, arg };
    }

    public Variable getResult() {
        return result;
    }
    
    @Override
    public Instr cloneForInlining(InlinerInfo ii) {
        return new Match2Instr((Variable) result.cloneForInlining(ii),
                receiver.cloneForInlining(ii), arg.cloneForInlining(ii));
    }

    @Override
    public Label interpret(ThreadContext context, IRubyObject self, IRubyObject[] args, Block block, Object exception, Object[] temp) {
        RubyRegexp regexp = (RubyRegexp) receiver.retrieve(context, self, temp);
        IRubyObject argValue = (IRubyObject) arg.retrieve(context, self, temp);
        result.store(context, self, temp, regexp.op_match(context, argValue));
        return null;
    }
}